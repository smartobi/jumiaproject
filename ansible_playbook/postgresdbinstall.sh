aws rds create-db-instance --db-name jumia_phone_validator \
 --db-instance-identifier jumiaPostDBIdentifier \
 --allocated-storage 20 \
 --db-instance-class db.m6i.large \
 --engine postgres \
 --master-username jumia \
 --master-user-password jumia123 \
 --vpc-security-group-ids sg-0ce201d5a12ae11c7 \
 --db-subnet-group-name "Jumia_Sub_Group_DB" \
 --port 5432


aws rds describe-db-instances  | grep Address