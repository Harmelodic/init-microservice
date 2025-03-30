package main

import rego.v1

name := input.metadata.name

deny contains msg if {
    input.kind == "Deployment"
    input.spec.strategy.type != "RollingUpdate"

    msg = sprintf("%s should have RollingUpdate strategy type for zero down-time rollouts", [name])
}

deny contains msg if {
	input.kind == "Deployment"
	not input.spec.template.spec.securityContext.runAsNonRoot

	msg = sprintf("Containers must not run as root in Deployment %s", [name])
}
