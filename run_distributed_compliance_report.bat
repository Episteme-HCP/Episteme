@echo off
echo ============================================================
echo Episteme Distributed Linear Algebra Compliance Report Runner
echo ============================================================
mvn test -pl episteme-benchmarks -Dtest=DistributedLinearAlgebraComplianceTest
echo REPORT GENERATED: docs/DISTRIBUTED_ALGEBRA_COMPLIANCE_REPORT.md
