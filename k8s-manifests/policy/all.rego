package main

import rego.v1

name := input.metadata.name

all_recommended_labels if {
	input.metadata.labels["app.kubernetes.io/name"]
	input.metadata.labels["app.kubernetes.io/instance"]
	input.metadata.labels["app.kubernetes.io/version"]
	input.metadata.labels["app.kubernetes.io/component"]
	input.metadata.labels["app.kubernetes.io/part-of"]
	input.metadata.labels["app.kubernetes.io/managed-by"]
}

deny contains msg if {
	not all_recommended_labels
	msg = sprintf("%s %s must include Kubernetes recommended labels: https://kubernetes.io/docs/concepts/overview/working-with-objects/common-labels/#labels", [input.kind, name])
}
