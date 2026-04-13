# 이 프로젝트의 Kubernetes 배포 리포트

## 1. 한눈에 보기

이 프로젝트의 `k8s` 폴더는 "이 마이크로서비스들을 쿠버네티스에서 어떻게 띄울지"를 적어둔 배포 설명서입니다.

쉽게 말하면:

- `db`: MySQL 데이터베이스 컨테이너
- `user`: 사용자/로그인 관련 서비스
- `product`: 상품 관련 서비스
- `order`: 주문 관련 서비스
- `delivery`: 배송 관련 서비스
- `gateway`: 바깥 요청을 받아서 내부 서비스로 전달하는 입구

즉, 이 프로젝트의 Kubernetes 배포는 "여러 개의 작은 서버를 하나의 클러스터 안에서 함께 운영"하기 위한 설정입니다.

---

## 2. Kubernetes 배포가 무엇인지

비전공자 기준으로 아주 단순하게 보면:

- **컨테이너**는 "앱을 포장한 실행 상자"입니다.
- **쿠버네티스(Kubernetes, k8s)** 는 그 상자들을 자동으로 띄우고, 연결하고, 관리하는 운영 도구입니다.
- **배포(Deployment)** 는 "이 컨테이너를 몇 개 띄울지, 어떤 이미지로 띄울지"를 정하는 설정입니다.

이 프로젝트에서는:

- 스프링부트 앱 4개(`user`, `product`, `order`, `delivery`)
- Nginx 게이트웨이 1개(`gateway`)
- MySQL DB 1개(`db`)

를 쿠버네티스 위에서 같이 실행합니다.

---

## 3. 전체 구조

### 3-1. 큰 흐름

사용자 요청 흐름은 아래처럼 보시면 됩니다.

```text
사용자 브라우저/클라이언트
        |
        v
     Ingress
        |
        v
    gateway-service
        |
        v
      gateway(Nginx)
   /login, /api/users      -> user-service
   /api/products           -> product-service
   /api/orders             -> order-service
   /api/deliveries         -> delivery-service

그리고 내부 서비스들은 공통으로
db-service(MySQL) 에 붙어 데이터를 읽고 씁니다.
```

### 3-2. 핵심 포인트

- 외부에서 직접 보이는 창구는 사실상 `gateway` 하나입니다.
- 나머지 서비스는 `ClusterIP` 서비스라서 클러스터 내부에서만 접근됩니다.
- 각 서비스는 `prod` 프로필로 실행되고, DB 접속 정보는 쿠버네티스가 환경변수로 넣어줍니다.
- 개발 환경에서는 각 서비스가 H2 메모리 DB를 쓰지만, 쿠버네티스 배포에서는 MySQL(`db-service`)를 사용합니다.

---

## 4. `k8s` 폴더가 하는 일

`k8s` 폴더에는 서비스별로 YAML 파일이 들어 있습니다.

보통 패턴은 아래와 같습니다.

- `*-configmap.yml`: 공개 가능한 설정값
- `*-secret.yml`: 비밀번호, 토큰 키 같은 민감 정보
- `*-deploy.yml` 또는 `*-deployment.yml`: 실제 컨테이너 실행 방법
- `*-service.yml`: 컨테이너를 내부 네트워크 이름으로 연결
- `gateway-ingress.yml`: 외부 요청을 게이트웨이로 받는 입구

즉, 이 폴더는 단순한 설정 모음이 아니라:

1. 어떤 앱을 띄우고
2. 어떤 포트로 열고
3. 어떤 비밀번호로 DB에 붙고
4. 외부 요청이 어디로 들어오고
5. 내부 서비스끼리는 어떤 이름으로 통신하는지

를 모두 정의하는 "운영용 지도"입니다.

---

## 5. 서비스별 역할

### 5-1. `db`

역할:

- 프로젝트의 공용 MySQL 데이터베이스

배포에서 하는 일:

- `db-deployment.yml`이 MySQL 컨테이너를 1개 띄웁니다.
- `db-service.yml`이 `db-service:3306` 이라는 내부 주소를 만듭니다.
- `db-secret.yml`이 MySQL 계정/비밀번호를 넣어줍니다.
- `db-configmap.yml`은 앱들이 사용할 DB 접속 URL 규칙을 담고 있습니다.

이 프로젝트에서 중요한 점:

- 모든 앱이 같은 DB 주소 `jdbc:mysql://db-service:3306/metadb...` 를 사용합니다.
- `db/init.sql`로 테이블과 샘플 데이터가 초기 생성됩니다.

즉, `db`는 전체 서비스가 함께 쓰는 공용 창고 같은 역할입니다.

### 5-2. `user`

역할:

- 회원, 로그인, 사용자 정보 관련 기능

배포에서 하는 일:

- `user-deploy.yml`이 `metacoding/user:1` 이미지를 실행합니다.
- `SPRING_PROFILES_ACTIVE=prod` 로 운영용 설정을 사용합니다.
- `user-configmap.yml`과 `user-secret.yml`을 읽어 DB 정보와 JWT 설정을 환경변수로 주입받습니다.
- `user-service.yml`이 내부 주소 `user-service:8083` 을 만듭니다.

이 서비스는 게이트웨이에서 아래 요청을 받습니다.

- `/login`
- `/api/users`

### 5-3. `product`

역할:

- 상품 목록, 상품 정보 관련 기능

배포에서 하는 일:

- `product-deploy.yml`이 `metacoding/product:1` 이미지를 실행합니다.
- `product-service.yml`이 내부 주소 `product-service:8082` 를 만듭니다.
- `product-secret.yml`에는 DB 계정과 JWT 관련 값도 포함되어 있습니다.

게이트웨이에서 아래 요청을 받습니다.

- `/api/products`

### 5-4. `order`

역할:

- 주문 생성, 주문 상태, 주문 내역 관련 기능

배포에서 하는 일:

- `order-deploy.yml`이 `metacoding/order:1` 이미지를 실행합니다.
- `order-service.yml`이 내부 주소 `order-service:8081` 를 만듭니다.
- DB 접속 정보는 `ConfigMap + Secret` 조합으로 주입받습니다.

게이트웨이에서 아래 요청을 받습니다.

- `/api/orders`

### 5-5. `delivery`

역할:

- 배송 정보와 배송 상태 관련 기능

배포에서 하는 일:

- `delivery-deploy.yml`이 `metacoding/delivery:1` 이미지를 실행합니다.
- `delivery-service.yml`이 내부 주소 `delivery-service:8084` 를 만듭니다.
- DB 접속 정보는 `ConfigMap + Secret` 조합으로 주입받습니다.

게이트웨이에서 아래 요청을 받습니다.

- `/api/deliveries`

### 5-6. `gateway`

역할:

- 외부 요청을 한 곳에서 받아 내부 서비스로 나눠 보내는 관문

이 프로젝트에서 특히 중요한 점:

- `gateway`는 스프링 클라우드 게이트웨이가 아니라 **Nginx 기반 프록시**입니다.
- `gateway/nginx.conf` 에서 URL 경로별로 어느 서비스로 보낼지 정합니다.

라우팅 규칙:

- `/login` -> `user-service:8083`
- `/api/users` -> `user-service:8083`
- `/api/products` -> `product-service:8082`
- `/api/orders` -> `order-service:8081`
- `/api/deliveries` -> `delivery-service:8084`

배포에서 하는 일:

- `gateway-deploy.yml`이 `metacoding/gateway:1` 이미지를 실행합니다.
- `gateway-service.yml`이 내부 주소 `gateway-service:80` 을 만듭니다.
- `gateway-ingress.yml`이 외부 HTTP 요청을 일단 `gateway-service`로 넘깁니다.

즉, 이 프로젝트에서 `gateway`는 "정문"입니다.

---

## 6. 요청이 실제로 어떻게 흐르는가

예를 들어 사용자가 상품 목록을 조회한다고 가정해보겠습니다.

1. 사용자가 브라우저나 Postman에서 `/api/products` 로 요청합니다.
2. Ingress가 이 요청을 `gateway-service`로 전달합니다.
3. `gateway` 컨테이너 안의 Nginx가 경로를 보고 `product-service:8082` 로 프록시합니다.
4. `product` 서비스가 요청을 처리합니다.
5. 필요하면 `db-service:3306` 의 MySQL에 접근해 데이터를 읽습니다.
6. 응답이 다시 `gateway`를 거쳐 사용자에게 돌아갑니다.

주문 조회나 배송 조회도 같은 방식입니다.

---

## 7. ConfigMap, Secret, Service, Deployment를 이 프로젝트에 맞춰 쉽게 설명하면

### 7-1. ConfigMap

`ConfigMap`은 "공개 가능한 설정 메모장"이라고 보면 됩니다.

예:

- DB URL
- DB 드라이버 이름

왜 쓰는가:

- 코드 안에 박아두지 않고 운영 환경에서 바꿀 수 있기 때문입니다.

### 7-2. Secret

`Secret`은 "민감한 정보 보관함"입니다.

예:

- DB 사용자명
- DB 비밀번호
- JWT 비밀키

왜 쓰는가:

- 비밀번호를 코드에 직접 넣지 않기 위해서입니다.

주의:

- 현재 이 저장소의 YAML에는 Secret 값이 평문으로 들어 있습니다.
- 학습용 예제로는 이해할 수 있지만, 실제 운영에서는 외부 비밀 관리 체계까지 함께 고려하는 것이 좋습니다.

### 7-3. Deployment

`Deployment`는 "이 앱 컨테이너를 어떻게 띄울지 설명하는 실행 계획서"입니다.

예:

- 어떤 이미지로 띄울지
- 포트는 몇 번인지
- 몇 개 복제본을 둘지
- 어떤 환경변수를 넣을지

이 프로젝트에서는 대부분 `replicas: 1` 입니다.

즉, 지금은 학습용/실습용 구조에 가깝고, 고가용성보다는 이해하기 쉬운 구성입니다.

### 7-4. Service

`Service`는 "쿠버네티스 내부 전화번호부"처럼 생각하면 쉽습니다.

예:

- `db-service`
- `user-service`
- `product-service`

왜 필요한가:

- Pod 이름은 바뀔 수 있지만, 서비스 이름은 고정되어 있어서 안정적으로 연결할 수 있기 때문입니다.

### 7-5. Ingress

`Ingress`는 "클러스터 바깥에서 안으로 들어오는 공식 입구"입니다.

이 프로젝트에서는:

- Ingress가 모든 요청을 `gateway-service`로 보냅니다.
- 실제 세부 라우팅은 `gateway`의 Nginx가 맡습니다.

즉, Ingress와 Gateway가 역할을 나눠서 쓰이고 있습니다.

---

## 8. 개발 환경과 Kubernetes 운영 환경의 차이

이 프로젝트는 `dev` 와 `prod` 가 다르게 설정되어 있습니다.

### 개발(`dev`)

- 기본 프로필은 `dev`
- 각 서비스가 H2 메모리 DB 사용
- 빠르게 로컬 테스트 가능
- 각 서비스의 `src/main/resources/db/data.sql` 로 샘플 데이터 주입

### Kubernetes(`prod`)

- Deployment에서 `SPRING_PROFILES_ACTIVE=prod`
- H2 대신 MySQL 사용
- DB 접속 정보는 쿠버네티스 환경변수로 주입
- 공용 DB 서비스 `db-service` 사용

쉽게 말하면:

- 로컬에서는 "혼자 빠르게 개발"
- 쿠버네티스에서는 "여러 서비스를 실제처럼 연결해서 운영"

입니다.

---

## 9. 이 프로젝트의 배포 순서

`README.md` 기준 배포 순서는 아래와 같습니다.

1. `minikube start`
2. 각 모듈 이미지를 빌드
3. `metacoding` 네임스페이스 생성
4. `kubectl apply -f k8s/...` 로 리소스 배포
5. `gateway-service` 주소로 접속

이미지 빌드 대상:

- `metacoding/db:1`
- `metacoding/order:1`
- `metacoding/product:1`
- `metacoding/user:1`
- `metacoding/delivery:1`
- `metacoding/gateway:1`

배포 순서를 보면, 이 프로젝트는 "코드 작성 -> 이미지 빌드 -> 쿠버네티스 배포" 흐름으로 학습하기 좋은 구조입니다.

---

## 10. 이 배포가 프로젝트에서 맡는 역할

이 프로젝트에서 Kubernetes 배포의 역할은 크게 5가지입니다.

### 1. 여러 서비스를 한 번에 운영하게 해줌

마이크로서비스는 서비스가 여러 개라 직접 각각 실행하고 연결하기 번거롭습니다.
쿠버네티스는 이를 한 번에 관리하게 해줍니다.

### 2. 서비스 간 통신을 이름으로 연결해줌

예:

- `user-service`
- `db-service`

이름만 알면 통신할 수 있어서 운영이 쉬워집니다.

### 3. 운영 환경 설정을 코드와 분리해줌

DB 주소, 비밀번호, JWT 키 같은 운영 설정을 YAML로 분리합니다.

### 4. 외부 요청을 한 입구로 모아줌

사용자는 각 서비스 주소를 몰라도 되고, `gateway`만 알면 됩니다.

### 5. 실제 운영과 비슷한 구조를 연습하게 해줌

이 프로젝트는 실무 축소판처럼:

- Gateway
- 내부 서비스
- 공용 DB
- 환경 분리(dev/prod)
- Ingress

를 한 번에 경험하게 해줍니다.

---

## 11. 공부할 때 꼭 이해하면 좋은 포인트

### 먼저 이해할 것

- 왜 서비스가 1개가 아니라 여러 개인가
- 왜 외부는 gateway만 열고 내부 서비스는 숨기는가
- 왜 `Service` 이름으로 통신하는가
- 왜 `dev` 와 `prod` 설정이 다른가
- 왜 DB 정보와 JWT 정보를 환경변수로 주입하는가

### 다음으로 보면 좋은 것

- `ClusterIP` 가 왜 내부 전용인지
- `Ingress` 와 `Gateway(Nginx)` 의 차이
- `ConfigMap` 과 `Secret` 의 차이
- `Deployment` 와 `Pod` 의 관계
- replicas를 늘리면 어떤 점이 달라지는지

---

## 12. 이 프로젝트 배포의 특징과 한계

특징:

- 구조가 단순해서 학습하기 좋음
- 마이크로서비스 기본 구성을 한 번에 볼 수 있음
- `gateway -> 각 서비스 -> db` 흐름이 명확함
- `dev` 와 `prod` 차이를 체감하기 좋음

한계:

- 모든 서비스가 `replicas: 1` 이라 장애 대응 구조는 단순함
- DB 영속 볼륨(PersistentVolume) 설정이 없음
- Secret 값이 저장소에 그대로 들어 있음
- 헬스체크(liveness/readiness) 설정이 없음
- 리소스 제한(cpu/memory requests, limits) 설정이 없음

즉, 실무 완성형 운영 구성이라기보다 "쿠버네티스 배포 개념을 익히기 좋은 교육용/실습용 구조"에 가깝습니다.

---

## 13. 결론

이 프로젝트의 `k8s` 폴더는 단순한 설정 모음이 아니라, 마이크로서비스 전체를 쿠버네티스 위에서 움직이게 만드는 운영 설계도입니다.

핵심만 다시 정리하면:

- `gateway`가 외부 요청을 받는 입구
- `user`, `product`, `order`, `delivery`가 실제 업무를 처리하는 서비스
- `db`가 공용 데이터를 저장하는 MySQL
- `Service`가 내부 연결을 담당
- `ConfigMap`과 `Secret`이 운영 설정을 주입
- `Deployment`가 실제 컨테이너 실행을 담당
- `Ingress`가 외부에서 안으로 들어오는 첫 관문

따라서 이 프로젝트에서 Kubernetes 배포의 역할은:

"여러 개의 서비스를 실제 운영처럼 연결하고, 설정을 주입하고, 외부 요청을 한 방향으로 정리해서 전체 시스템이 함께 돌아가게 만드는 것"

이라고 이해하시면 됩니다.

---

## 14. 참고한 실제 파일

- `README.md`
- `k8s/db/*`
- `k8s/user/*`
- `k8s/product/*`
- `k8s/order/*`
- `k8s/delivery/*`
- `k8s/gateway/*`
- `gateway/nginx.conf`
- 각 서비스의 `application-prod.properties`
- `db/init.sql`

