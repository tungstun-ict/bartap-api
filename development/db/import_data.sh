#!/bin/bash
set -e

echo "Importing words (source: OpenTaal)"

PGPASSWORD=${DB_APP_PASS} psql --username ${DB_APP_USER} ${DB_APP_NAME} --file /data/bar_data.sql