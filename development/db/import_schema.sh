#!/bin/bash
set -e

echo "Importing database schema"

PGPASSWORD=${DB_APP_PASS} psql --username ${DB_APP_USER} ${DB_APP_NAME} --file /data/schema.sql
