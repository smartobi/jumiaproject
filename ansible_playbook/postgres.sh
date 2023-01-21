#!/bin/bash
aws rds create-db-subnet-group \
    --db-subnet-group-name mypostgress \
    --db-subnet-group-description "Jumia DB subnet group" \
    --subnet-ids '["subnet-0fc00c2ba14260504","subnet-009b61b3c6ecb5b99","subnet-03eb06d882ab2f736"]'
