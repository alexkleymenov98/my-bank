{{/*
Common labels for Grafana resources.
*/}}
{{- define "grafana.labels" -}}
helm.sh/chart: {{ printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" }}
app.kubernetes.io/name: grafana
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
app.kubernetes.io/part-of: bank
app.kubernetes.io/component: dashboards
{{- end }}

{{/*
Selector labels.
*/}}
{{- define "grafana.selectorLabels" -}}
app.kubernetes.io/name: grafana
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}