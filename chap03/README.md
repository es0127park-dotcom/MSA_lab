#### 1 미니큐브 시작
```bash
minikube start
```
#### 2 이미지 빌드

```bash
# 프로젝트 루트에서 실행
minikube image build -t metacoding/db:2 ./db
minikube image build -t metacoding/gateway:2 ./gateway
minikube image build -t metacoding/order:2 ./order
minikube image build -t metacoding/product:2 ./product
minikube image build -t metacoding/user:2 ./user
minikube image build -t metacoding/delivery:2 ./delivery
minikube image build -t metacoding/orchestrator:2 ./orchestrator
```

#### 3 네임스페이스 생성

```bash
kubectl create namespace metacoding
```

#### 4 Kubernetes 리소스 배포

```bash
# Kafka 먼저 배포 (다른 서비스들이 Kafka에 의존)
kubectl apply -f k8s/kafka

# Kafka가 준비될 때까지 대기
kubectl wait --for=condition=ready pod -l app=kafka -n metacoding --timeout=120s

# 다른 서비스 배포
kubectl apply -f k8s/db
kubectl apply -f k8s/order
kubectl apply -f k8s/product
kubectl apply -f k8s/user
kubectl apply -f k8s/delivery
kubectl apply -f k8s/gateway
kubectl apply -f k8s/orchestrator
```

### 5. 서비스 접근

```bash
minikube service gateway-service -n metacoding --url
```

### 6. 상태 확인

```bash
# Pod 상태 확인
kubectl get pods -n metacoding

# Service 확인
kubectl get services -n metacoding

# Deployment 상태 확인
kubectl get deployments -n metacoding

# Pod 로그 확인
kubectl logs -n metacoding <pod-name>
```

### 7. 이미지 리스타트

```bash
# Deployment 재시작 
kubectl rollout restart deployment/gateway-deploy -n metacoding
kubectl rollout restart deployment/gateway-deploy -n metacoding
kubectl rollout restart deployment/order-deploy -n metacoding
kubectl rollout restart deployment/product-deploy -n metacoding
kubectl rollout restart deployment/user-deploy -n metacoding
kubectl rollout restart deployment/delivery-deploy -n metacoding
kubectl rollout restart deployment/orchestrator-deploy -n metacoding
```
