# Coding Exercise

This repository contains my proposed solution for a given coding exercise. The coding exercise has the following requirements.

## Requirements

* Must be a native Android app written in Kotlin or Java.
* Must retrieve data from [here](https://fetch-hiring.s3.amazonaws.com/hiring.json).
* The retrieved list of items needs to be displayed to the user based on the following requirements:
  * Display all the items grouped by "listId".
  * Sort the results first by "listId" then by "name" when displaying.
  * Filter out any items where "name" is blank or null.
* The final result should be displayable to the user in an easy-to-read list.
* Project must be buildable on the latest (non-pre release) tools and support the current release mobile OS.
* Solution must be posted in a public repository, such as GitHub or BitBucket.

## Description

Based on the requirements, the provided app will immediately start retrieving the data from the given source once displayed. While the data is being retrieved and processed, a message will be displayed to the user indicating that the data is being retrieved and processed. Once processing is complete, the data will be displayed on-screen in an easy-to-read list.

When displaying the items, the naming format will be `<listId> <name>`. Despite the retrieved data including an `id` element, this will not be included in the output because it was never mentioned in any of the requirements.

Lastly, this exercise assumes the source URL will always be available, the data format will remain unchanged, and re-requesting data is unnecessary.