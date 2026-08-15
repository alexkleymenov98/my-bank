{{/*
Helpers for the Zipkin subchart. Shape matches the bank chart family:
  - name           - chart name unless overridden
  - fullname       - "<release>-<chart>" so multiple installs don't clash
  - labels         - Helm best-practice labels + app=<name> for selectors
  - selectorLabels - immutable subset (no version/chart) used in Deployment
*/}}

{{- define "zipkin.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "zipkin.fullname" -}}
{{- if .Values.fullnameOverride -}}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- $name := default .Chart.Name .Values.nameOverride -}}
{{- if contains $name .Release.Name -}}
{{- .Release.Name | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}
{{- end -}}

{{- define "zipkin.labels" -}}
helm.sh/chart: {{ printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
app.kubernetes.io/name: {{ include "zipkin.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
app.kubernetes.io/component: tracing
app.kubernetes.io/part-of: bank
app: {{ include "zipkin.name" . }}
{{- end -}}

{{- define "zipkin.selectorLabels" -}}
app.kubernetes.io/name: {{ include "zipkin.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app: {{ include "zipkin.name" . }}
{{- end -}}

{{- define "zipkin.image" -}}
{{- $registry := .Values.global.imageRegistry | default "" -}}
{{- $repo := .Values.image.repository -}}
{{- $tag := .Values.image.tag | default .Chart.AppVersion -}}
{{- if $registry -}}
{{- printf "%s/%s:%s" $registry $repo $tag -}}
{{- else -}}
{{- printf "%s:%s" $repo $tag -}}
{{- end -}}
{{- end -}}
