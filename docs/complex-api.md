<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Develop a More Complex API](#develop-a-more-complex-api)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Develop a More Complex API

We've got most of the things we want for Concept22, so now it's time to expand the API 
to include the things we'll want in the future:

1. [X] User Login
2. [X] Subscriptions to data sources
   1. [X] using CSRF tokens
   2. [ ] implement multi-source subscriptions (just pass a vector)
3. [ ] Wire subscriptions into the data-sources
   1. we need a common mechanism, supported by all data-sources, so we can do an initial "sync" with new clients
4. [ ] Personalization
   1. [ ] save-layout
   2. [ ] load-layout


