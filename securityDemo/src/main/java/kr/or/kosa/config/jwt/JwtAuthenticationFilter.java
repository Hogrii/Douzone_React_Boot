package kr.or.kosa.config.jwt;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.kosa.config.auth.PrincipalDetails;
import kr.or.kosa.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	private final AuthenticationManager authenticationManager;
	
	// Authentication 객체 만들어서 리턴 => 의존 : AuthenticationManager
	// 인증 요청시에 실행되는 URL => /login  
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		System.out.println("JwtAuthenticationFilter : 진입");
		
		// request에 있는 username과 password를 파싱해서 자바 Object로 받기
		// 로그인시 들어오는 json 데이터를 객체로 변환한다
		ObjectMapper om = new ObjectMapper();
		// 로그인 요청 객체
		LoginRequestDto loginRequestDto = null;
		try {
			loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("JwtAuthenticationFilter : "+loginRequestDto);
		
		// 유저네임패스워드 토큰 생성
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						loginRequestDto.getUsername(), 
						loginRequestDto.getPassword());
		
		System.out.println("JwtAuthenticationFilter : 토큰생성완료");
		
		// authenticate() 함수가 호출 되면 인증 프로바이더가 유저 디테일 서비스의
		// loadUserByUsername(토큰의 첫번째 파라메터) 를 호출하고
		// UserDetails를 리턴받아서 토큰의 두번째 파라메터(credential)과
		// UserDetails(DB값)의 getPassword()함수로 비교해서 동일하면
		// Authentication 객체를 만들어서 필터체인으로 리턴해준다.
		
		// 인증 프로바이더의 디폴트 서비스는 UserDetailsService 타입
		// 인증 프로바이더의 디폴트 암호화 방식은 BCryptPasswordEncoder
		// 결론은 인증 프로바이더에게 알려줄 필요가 없음.
		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		
		PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("Authentication : "+principalDetailis.getUser().getUsername());
		return authentication;
	}

	// JWT Token 생성해서 response에 담아주기
	// 인증이 끝나고 후처리 작업
	// 토큰을 만들고 헤더에 담아준다
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		System.out.println("JwtAuthenticationFilter : successfulAuthentication() 호출");
		//인증된 객체 얻기  
		PrincipalDetails principalDetailis = (PrincipalDetails) authResult.getPrincipal();

		//인증키 생성(암호화 키)
		SecretKey key = Keys.hmacShaKeyFor(JwtProperties.getSecretKey());
		//인증 토큰 생성 
		//클래임 객체에 id, username 키로 정보를 추가한다
		String jwtToken = Jwts.builder()
				.setClaims(Map.of("id", principalDetailis.getUser().getId()
								, "username", principalDetailis.getUser().getUsername()))
				//토큰의 사용 기간을 설정한다(현재 시스템 시간 + 만료 기간)
				.setExpiration(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
				// 암호화 키
				.signWith(key)
				.compact();

		//인증 토큰 출력 
		System.out.println(JwtProperties.HEADER_STRING + "=" + JwtProperties.TOKEN_PREFIX+jwtToken);
		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);
	}
	
}
