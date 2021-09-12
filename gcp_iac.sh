#!/bin/bash
gcloud iam service-accounts create push-article-reader
gcloud pubsub topics create article-read-events \
  --message-retention-duration=7d
gcloud projects add-iam-policy-binding push-to-date \
  --member=serviceAccount:push-article-reader@push-to-date.iam.gserviceaccount.com \
  --role=roles/pubsub.publisher