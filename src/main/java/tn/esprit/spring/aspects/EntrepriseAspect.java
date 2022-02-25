package tn.esprit.spring.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EntrepriseAspect {

	private static final Logger LOG = LoggerFactory.getLogger(EntrepriseAspect.class);

	@AfterThrowing(value = "execution(* tn.esprit.spring.services.EntrepriseServiceImpl.*(..))", throwing = "ex")
	public void logException(JoinPoint joinPoint, Exception ex) {
		LOG.error("Exception in method {}" , joinPoint.getSignature());
		LOG.error("Exception is {}" , ex.getMessage());
	}

	@Before("execution(* tn.esprit.spring.services.EntrepriseServiceImpl.*(..))")
	public void logMethodEntry(JoinPoint joinPoint) {
		LOG.info("Called {}", joinPoint.getSignature().getName());
	}

	@Before("execution(* tn.esprit.spring.services.EntrepriseServiceImpl.*(..))")
	public void logMethodCompletion(JoinPoint joinPoint) {
		LOG.info("Completed {}", joinPoint.getSignature().getName());
	}

	@Around("execution(* tn.esprit.spring.services.EntrepriseServiceImpl.*(..))")
	public Object profile(ProceedingJoinPoint pjp) throws Throwable {
		LOG.debug("In point cut");
		long start = System.currentTimeMillis();
		Object obj = pjp.proceed();
		long elapsedTime = System.currentTimeMillis() - start;
		if (elapsedTime > 3000) {
			LOG.warn("This process takes more than 3sec to execute");
		}
		LOG.info("Method execution time {}" , elapsedTime);
		return obj;
	}

}