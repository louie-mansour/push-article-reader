#!/bin/bash

# Compute Engine default service account bindings (Needed for Cloud Run)
gcloud projects add-iam-policy-binding push-to-date \
  --member=822734827399-compute@developer.gserviceaccount.com  \
  --role=roles/run.admin --role=roles/iam.serviceAccountUser

# CI service account and policy bindings
gcloud iam service-accounts create push-article-reader-ci
gcloud projects add-iam-policy-binding push-to-date \
  --member=serviceAccount:push-article-reader-ci@push-to-date.iam.gserviceaccount.com \
  --role=roles/storage.admin --role=roles/run.admin --role=roles/iam.serviceAccountUser

# App service account and policy bindings
gcloud iam service-accounts create push-article-reader-app
gcloud projects add-iam-policy-binding push-to-date \
  --member=serviceAccount:push-article-reader-app@push-to-date.iam.gserviceaccount.com \
  --role=roles/pubsub.publisher

# Cloud Scheduler account and policy bindings
gcloud iam service-accounts create push-article-reader-scheduler
gcloud run services add-iam-policy-binding push-article-reader-run \
   --member=serviceAccount:push-article-reader-scheduler@push-to-date.iam.gserviceaccount.com \
   --role=roles/run.invoker

# Cloud Scheduler job
gcloud scheduler jobs create http push-article-reader-scheduler --schedule "* * * * *" \
  --http-method=POST \
  --uri="https://push-article-reader-run-izmeb2u2pa-nn.a.run.app/migrate?sources=hacker-news" \
  --oidc-service-account-email=push-article-reader-scheduler@push-to-date.iam.gserviceaccount.com \
  --oidc-token-audience="https://push-article-reader-run-izmeb2u2pa-nn.a.run.app/migrate?sources=hacker-news"

# Pubsub
gcloud pubsub topics create article-read-events \
  --message-retention-duration=7d