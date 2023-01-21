#!/bin/bash
aws rds create-db-subnet-group \
    --db-subnet-group-name Jumia_Sub_Group_postgress \
    --db-subnet-group-description "Jumia DB subnet group" \
    --subnet-ids '["subnet-0a3b2cf38dee9bd78","subnet-0deb0ccf87d458366","subnet-08e4e84f34858d0d0"]'