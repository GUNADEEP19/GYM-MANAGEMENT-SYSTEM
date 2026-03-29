# Upgrade Plan: gym-management-system-backend (20260329131044)

- **Generated**: 2026-03-29 18:43:00 IST
- **HEAD Branch**: feature/user-system
- **HEAD Commit ID**: bf479f5

## Available Tools

**JDKs**
- JDK 25.0.1: /Library/Java/JavaVirtualMachines/jdk-25.jdk/Contents/Home/bin (used by steps 2-4)

**Build Tools**
- Maven Wrapper 3.9.14: ./mvnw (used by steps 2-4)
- Maven 3.9.13: /opt/homebrew/Cellar/maven/3.9.13/bin (fallback only)

## Guidelines

- Upgrade Spring Framework to 6.2.x while preserving existing application behavior.
- Use the smallest viable dependency/configuration changes needed to meet the target.

> Note: You can add any specific guidelines or constraints for the upgrade process here if needed, bullet points are preferred.

## Options

- Working branch: appmod/java-upgrade-20260329131044
- Run tests before and after the upgrade: true

## Upgrade Goals

- Upgrade Spring Framework from 6.1.18 to 6.2.x.

### Technology Stack

| Technology/Dependency | Current | Min Compatible | Why Incompatible |
| --------------------- | ------- | -------------- | ---------------- |
| Java | 25 | 17 | - |
| Spring Boot (parent) | 3.3.10 | 3.4.0 | Spring Boot 3.3.10 manages Spring Framework 6.1.18; Spring Framework 6.2 requires Boot 3.4+ BOM alignment. |
| Spring Framework (spring-context resolved) | 6.1.18 | 6.2.0 | User target requires 6.2.x. |
| Spring Data JPA | 3.3.10 | 3.4.0 | Must stay aligned with Spring Boot BOM after parent upgrade. |
| Maven Wrapper | 3.9.14 | 3.9.14 | Compatible for this dependency upgrade scope. |
| maven-compiler-plugin | Managed by Boot 3.3.10 | Managed by Boot 3.4.0 | Keep plugin stack aligned with upgraded Boot parent. |
| maven-surefire-plugin | Managed by Boot 3.3.10 | Managed by Boot 3.4.0 | Keep test plugin stack aligned with upgraded Boot parent. |

### Derived Upgrades

- Upgrade Spring Boot parent from 3.3.10 to 3.4.0 so managed Spring Framework moves from 6.1.18 to 6.2.0.
- Keep Spring Framework managed by Spring Boot BOM for dependency coherence across starters and plugins.
- Remove the explicit org.springframework:spring-context direct dependency to avoid redundant manual pinning and keep version governance centralized in the Boot BOM.

## Upgrade Steps

- Step 1: Setup Environment
  - **Rationale**: Confirm required tools are present before running baseline and upgrade steps.
  - **Changes to Make**:
    - [ ] Verify JDK 25 availability for current project target level.
    - [ ] Verify Maven Wrapper executable and usable.
  - **Verification**:
    - Command: `#appmod-list-jdks` and `./mvnw --version`
    - Expected: JDK 25 and Maven Wrapper available.

- Step 2: Setup Baseline
  - **Rationale**: Establish pre-upgrade compile and test status.
  - **Changes to Make**:
    - [ ] Run baseline compile for both main and test sources.
    - [ ] Run baseline test suite and record pass/fail counts.
  - **Verification**:
    - Command: `./mvnw clean test-compile -q && ./mvnw clean test -q`
    - JDK: /Library/Java/JavaVirtualMachines/jdk-25.jdk/Contents/Home/bin
    - Expected: Baseline compile/test results documented.

- Step 3: Upgrade to Spring Framework 6.2 via Spring Boot BOM
  - **Rationale**: Spring Framework version is BOM-managed by Spring Boot parent; upgrading parent is the minimal coherent way to reach 6.2.
  - **Changes to Make**:
    - [ ] Update `spring-boot-starter-parent` from 3.3.10 to 3.4.0.
    - [ ] Remove explicit `org.springframework:spring-context` dependency to avoid redundant pinning.
    - [ ] Re-run dependency resolution and fix any compile issues.
  - **Verification**:
    - Command: `./mvnw clean test-compile -q`
    - JDK: /Library/Java/JavaVirtualMachines/jdk-25.jdk/Contents/Home/bin
    - Expected: Compilation success with resolved Spring Framework 6.2.x.

- Step 4: Final Validation
  - **Rationale**: Confirm all targets are met and tests pass at 100%.
  - **Changes to Make**:
    - [ ] Verify effective Spring Framework version is 6.2.x.
    - [ ] Run clean compile and full test suite.
    - [ ] Fix any remaining compile/test failures until all pass.
  - **Verification**:
    - Command: `./mvnw clean test -q`
    - JDK: /Library/Java/JavaVirtualMachines/jdk-25.jdk/Contents/Home/bin
    - Expected: Compilation success and 100% tests pass.

## Key Challenges

- **BOM-Managed Framework Versioning**
  - **Challenge**: Spring Framework is managed by Spring Boot BOM, so direct upgrades can introduce version drift.
  - **Strategy**: Upgrade the Spring Boot parent to the minimum line that provides Spring 6.2 and keep direct Spring module declarations unversioned or remove redundancies.

- **Runtime/Plugin Alignment with Java 25**
  - **Challenge**: Build/test plugins and compiler settings must remain compatible after parent upgrade.
  - **Strategy**: Keep plugin versions managed by the same Boot BOM and validate with clean test-compile and full test runs.
