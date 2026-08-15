{{/*
Common labels for every ELK resource.
*/}}
{{- define "elk.labels" -}}
helm.sh/chart: {{ printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
app.kubernetes.io/part-of: bank
{{- end }}

{{- define "elk.elasticsearch.labels" -}}
{{ include "elk.labels" . }}
app.kubernetes.io/name: elasticsearch
app.kubernetes.io/component: storage
{{- end }}

{{- define "elk.elasticsearch.selectorLabels" -}}
app.kubernetes.io/name: elasticsearch
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{- define "elk.logstash.labels" -}}
{{ include "elk.labels" . }}
app.kubernetes.io/name: logstash
app.kubernetes.io/component: aggregator
{{- end }}

{{- define "elk.logstash.selectorLabels" -}}
app.kubernetes.io/name: logstash
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{- define "elk.kibana.labels" -}}
{{ include "elk.labels" . }}
app.kubernetes.io/name: kibana
app.kubernetes.io/component: ui
{{- end }}

{{- define "elk.kibana.selectorLabels" -}}
app.kubernetes.io/name: kibana
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}