# 🏗️ 마이크로서비스 핵심 공통 로직(Core) 가이드 리포트

`core` 폴더는 서비스의 본래 기능(주문, 상품 등) 외에 **보안(Filter), 예외 처리(Handler), 공통 응답(Util), 설정(Config)** 등 서비스가 안정적으로 돌아가기 위한 '기반 시설'을 모아둔 곳입니다.

---

## 1. 🛡️ filter 폴더: 보안의 첫 번째 관문 (Security Guard)

> **🎭 비유: 클럽 입구의 보안 요원(Bouncer)**
> 손님이 클럽에 들어오기 전, 입구에서 신분증(JWT)을 검사하는 요원입니다. 신분증이 가짜이거나 없으면 입장을 거부하고, 진짜라면 "이 사람은 몇 번 테이블 손님이다"라고 표시(userId 저장)를 해준 뒤 들여보냅니다.

### 📄 JwtAuthenticationFilter.java
*   **언제 동작하나?** HTTP 요청이 **컨트롤러에 도달하기 전**에 가로챕니다.
*   **역할:** 요청 헤더의 JWT 토큰을 꺼내 검증하고, 유효하면 사용자 정보를 시스템에 심어줍니다.

```java
// 핵심 메서드: doFilterInternal
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
    // 1. 요청 헤더에서 "Authorization" 키로 토큰을 꺼냄
    String prefixToken = request.getHeader(JwtUtil.HEADER);

    // 2. 토큰이 없거나 "Bearer "로 시작하지 않으면 그냥 통과 (로그인이 필요 없는 요청일 수 있음)
    if (prefixToken == null || !prefixToken.startsWith(JwtUtil.TOKEN_PREFIX)) {
        filterChain.doFilter(request, response);
        return;
    }

    // 3. 토큰에서 "Bearer "를 떼어내고 순수한 토큰값만 추출
    String token = prefixToken.replace(JwtUtil.TOKEN_PREFIX, "");

    // 4. [검증] JwtUtil을 통해 토큰이 유효한지(변조/만료 여부) 확인
    if (jwtUtil.validateToken(token)) {
        // 5. [중요] 토큰 안에 들어있는 userId를 꺼내서 request 객체에 저장!
        // 이렇게 저장해두면 컨트롤러에서 @RequestAttribute("userId")로 꺼내 쓸 수 있음
        int userId = jwtUtil.getUserId(token);
        request.setAttribute("userId", userId);
    }
    
    // 6. 다음 필터나 컨트롤러로 요청을 넘겨줌
    filterChain.doFilter(request, response);
}
```

---

## 2. 🚨 handler 폴더: 시스템의 안전망 (Safety Net)

> **🎭 비유: 119 종합 상황실 (Emergency Center)**
> 건물 내부(Service, Controller) 어디선가 불이 나거나 사고(Exception)가 발생하면, 즉시 상황실로 연락이 갑니다. 상황실은 사고의 종류(404, 500 등)에 따라 구급차를 보낼지 소방차를 보낼지 결정하고, 상황을 정리하여 밖으로 알려줍니다.

### 📄 GlobalExceptionHandler.java
*   **언제 동작하나?** 서비스 로직(`Service`, `Controller` 등)에서 `throw new ExceptionXXX()`가 실행될 때 작동합니다.
*   **역할:** 에러의 종류에 따라 적절한 HTTP 상태 코드와 메시지를 JSON으로 응답합니다.

```java
@RestControllerAdvice // [개념] 모든 컨트롤러에서 발생하는 예외를 감시하는 어노테이션
public class GlobalExceptionHandler {

    // 404 에러(데이터 없음)가 발생했을 때 호출됨
    @ExceptionHandler(Exception404.class)
    public ResponseEntity<?> handle404(Exception404 e) {
        // e.getMessage()에 담긴 에러 내용을 Resp.fail()에 담아 404 상태코드로 응답
        return new ResponseEntity<>(Resp.fail(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    // 500 에러(서버 내부 오류)가 발생했을 때 호출됨
    @ExceptionHandler(Exception500.class)
    public ResponseEntity<?> handle500(Exception500 e) {
        // 보안상 실제 에러 내용은 숨기고 "서버 내부 오류"라는 메시지만 전달
        return new ResponseEntity<>(Resp.fail("서버 내부 오류가 발생했습니다."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

---

## 3. 🛠️ util 폴더: 공통 도구함 (Common Tools)

> **🎭 비유: 규격화된 택배 상자 (Standardized Box)**
> 내용물이 옷이든 음식이든 전자기기든, 고객에게 배송할 때는 항상 똑같은 모양의 규격화된 택배 상자에 담아 보냅니다. 받는 사람(클라이언트)은 상자 모양이 항상 같으니 내용물만 쏙 빼서 확인하기가 매우 편리해집니다.

### 📄 Resp.java (Response Utility)
*   **역할:** 모든 API 응답을 `{ "status": 200, "msg": "성공", "body": { ... } }` 형태로 통일합니다.

```java
public class Resp {
    // 1. 성공 시 호출: body에 실제 데이터를 담아서 200 OK 응답
    public static <T> ResponseEntity<?> ok(T body) {
        return ResponseEntity.ok(new RespDTO<>(200, "성공", body));
    }

    // 2. 실패 시 호출: 에러 메시지를 담아서 응답 (상태코드는 Handler에서 결정)
    public static ResponseEntity<?> fail(String msg) {
        return ResponseEntity.ok(new RespDTO<>(-1, msg, null));
    }
}
```

---

## 4. ⚙️ config 폴더: 시스템 설계도 (Blueprint)

> **🎭 비유: 외교 전담 전령 (Diplomatic Courier)**
> 왕궁(Order 서비스)에서 이웃 나라(Product 서비스)로 심부름을 보낼 때, 전령에게 왕의 인장(사용자 토큰)을 맡깁니다. 전령은 이 인장을 들고 가서 이웃 나라 문지기에게 보여주고 정당한 요청임을 증명합니다.

### 📄 RestClientConfig.java (Order 서비스의 핵심)
*   **역할:** 서비스가 다른 서비스(예: Order -> Product)를 호출할 때 **인증 정보를 복사(Token Relay)**합니다.

```java
@Bean
public RestClient.Builder restClientBuilder() {
    // [인터셉터]: 요청을 보내기 직전에 가로채서 헤더를 조작함
    ClientHttpRequestInterceptor authForwardingInterceptor = (request, body, execution) -> {
        // 1. 현재 이 서비스를 요청한 클라이언트의 정보를 가져옴
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        
        if (attributes != null) {
            // 2. [Token Relay]: 원래 요청에 있던 Authorization(JWT) 헤더를 꺼내서
            String authorization = attributes.getRequest().getHeader("Authorization");
            if (authorization != null) {
                // 3. 다음 서비스로 나가는 요청 헤더에 똑같이 넣어줌
                request.getHeaders().add("Authorization", authorization);
            }
        }
        return execution.execute(request, body); // 요청 계속 진행
    };
    return RestClient.builder().requestInterceptor(authForwardingInterceptor);
}
```

---

## 💡 MSA 핵심 기술 용어 정리

1.  **AOP (Aspect Oriented Programming)**
    *   **🎭 비유: 아파트의 공용 관리비/경비 시스템**
    *   각 세대(비즈니스 로직)가 각자 경비를 서는 게 아니라, 아파트 전체 차원에서 경비실(AOP)을 두고 보안을 관리하는 것과 같습니다.
2.  **무상태성 (Stateless)**
    *   **🎭 비유: 일일 입장권을 사용하는 놀이공원**
    *   직원이 내 얼굴을 기억(세션)하지 않아도, 내가 들고 있는 입장권(토큰)만 보여주면 언제든 놀이기구를 탈 수 있는 방식입니다.
3.  **인증 전파 (Token Relay)**
    *   **🎭 비유: 이어달리기 바통 터치**
    *   첫 번째 주자(Order)가 들고 온 권한 바통을 두 번째 주자(Product)에게 그대로 넘겨주어, 팀 전체가 유효한 경기를 이어가는 것입니다.
4.  **응답 정형화 (Response Standardization)**
    *   **🎭 비유: 전 세계 공통 전기 플러그**
    *   어느 나라(어느 서비스)를 가도 플러그 모양이 같으면, 여행자(클라이언트)는 어댑터 고민 없이 기기를 바로 꽂아 쓸 수 있습니다.
