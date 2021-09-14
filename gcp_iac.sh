#!/bin/bash

# Compute Engine default service account bindings (Needed for Cloud Run)
gcloud projects add-iam-policy-binding push-to-date \
  --member=serviceAccount:822734827399-compute@developer.gserviceaccount.com  \
  --role=roles/iam.serviceAccountUser
gcloud projects add-iam-policy-binding push-to-date \
  --member=serviceAccount:822734827399-compute@developer.gserviceaccount.com  \
  --role=roles/run.admin

# CI service account and policy bindings
gcloud iam service-accounts create push-article-reader-ci
gcloud projects add-iam-policy-binding push-to-date \
  --member=serviceAccount:push-article-reader-ci@push-to-date.iam.gserviceaccount.com \
  --role=roles/iam.serviceAccountUser
gcloud projects add-iam-policy-binding push-to-date \
  --member=serviceAccount:push-article-reader-ci@push-to-date.iam.gserviceaccount.com \
  --role=roles/run.admin
gcloud projects add-iam-policy-binding push-to-date \
  --member=serviceAccount:push-article-reader-ci@push-to-date.iam.gserviceaccount.com \
  --role=roles/storage.admin

# App service account and policy bindings
gcloud iam service-accounts create push-article-reader-run
gcloud projects add-iam-policy-binding push-to-date \
  --member=serviceAccount:push-article-reader-run@push-to-date.iam.gserviceaccount.com \
  --role=roles/pubsub.publisher

# Cloud Run create and assign service account
gcloud run deploy push-article-reader-run \
  --image=gcr.io/push-to-date/push-article-reader:latest \
  --ingress=all \
  --min-instances=1 \
  --max-instances=1 \
  --timeout=5m \
  --region=northamerica-northeast1 \
  --no-allow-unauthenticated \
  --service-account=push-article-reader-run@push-to-date.iam.gserviceaccount.com

# Cloud Scheduler account and policy bindings
gcloud iam service-accounts create push-article-reader-scheduler
gcloud run services add-iam-policy-binding push-article-reader-run \
  --region=northamerica-northeast1 \
  --member=serviceAccount:push-article-reader-scheduler@push-to-date.iam.gserviceaccount.com \
  --role=roles/run.invoker

# Cloud Scheduler job
gcloud scheduler jobs create http push-article-reader-scheduler --schedule "0 * * * *" \
  --http-method=POST \
  --uri="https://push-article-reader-run-izmeb2u2pa-nn.a.run.app/migrate?sources=hacker-news" \
  --oidc-service-account-email=push-article-reader-scheduler@push-to-date.iam.gserviceaccount.com \
  --oidc-token-audience="https://push-article-reader-run-izmeb2u2pa-nn.a.run.app/migrate?sources=hacker-news"

# PubSub
gcloud pubsub topics create article-read-events \
  --message-retention-duration=7d
gcloud auth application-default login