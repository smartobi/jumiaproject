aws rds create-db-instance --db-name jumia_phone_validator \
 --db-instance-identifier jumiaDbIdentifier \
 --allocated-storage 20 \
 --db-instance-class db.m6i.large \
 --engine postgres \
 --master-username jumia \
 --master-user-password jumia123 \
 --vpc-security-group-ids sg-098b3661b1a019c2e \
 --db-subnet-group-name "mypostgress" \
 --port 5432


aws rds describe-db-instances  | grep Address