apiVersion: apps/v1
kind: Deployment
metadata:
  name: ${USER_NAME}-${SERVICE_NAME}
  namespace: ${NAMESPACE}
spec:
  replicas: 1
  selector:
    matchLabels:
      app: ${USER_NAME}-${SERVICE_NAME}
  template:
    metadata:
      annotations:
        prometheus.io/scrape: 'true'
        prometheus.io/port: '8081'
        prometheus.io/path: '/actuator/prometheus'
        update: ${HASHCODE}
      labels:
        app: ${USER_NAME}-${SERVICE_NAME}
    spec:
      serviceAccountName: default
      containers:
      - name: ${IMAGE_NAME}
        image: ${DOCKER_REGISTRY}/${USER_NAME}-${IMAGE_NAME}:${VERSION}
        imagePullPolicy: Always
        env:
        - name: LOGGING_LEVEL
          value: ${LOGGING_LEVEL}
        - name: USER_NAME
          value: ${USER_NAME}
        - name: NAMESPACE
          value: ${NAMESPACE}
        - name: SPRING_PROFILES_ACTIVE
          value: ${PROFILE}
        - name: SPRING_DATASOURCE_URL
          valueFrom:
            configMapKeyRef:
              name: sk000-${SERVICE_NAME}-config
              key: SPRING_DATASOURCE_URL
        - name: SPRING_DATASOURCE_USERNAME
          valueFrom:
            configMapKeyRef:
              name: sk000-${SERVICE_NAME}-config
              key: SPRING_DATASOURCE_USERNAME
        envFrom:
        - secretRef:
            name: sk000-${SERVICE_NAME}-secrets
