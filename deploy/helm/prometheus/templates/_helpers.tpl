{{/*
Common labels for all Prometheus stack resources.
*/}}
{{- define "prometheus.labels" -}}
helm.sh/chart: {{ printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
app.kubernetes.io/part-of: bank
{{- end }}

{{/*
Prometheus-specific labels.
*/}}
{{- define "prometheus.prometheus.labels" -}}
{{ include "prometheus.labels" . }}
app.kubernetes.io/name: prometheus
app.kubernetes.io/component: server
{{- end }}

{{/*
Alertmanager-specific labels.
*/}}
{{- define "prometheus.alertmanager.labels" -}}
{{ include "prometheus.labels" . }}
app.kubernetes.io/name: alertmanager
app.kubernetes.io/component: alerting
{{- end }}