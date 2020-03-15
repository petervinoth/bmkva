package com.bm.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bm.Model.ConfirmationToken;
import com.bm.Model.User;
import com.bm.Repository.ConfirmationTokenRepository;
import com.bm.Repository.UserRepository;
import com.bm.Service.EmailSenter;

@Controller
public class UserController {

	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;
	
	@Autowired
	private EmailSenter emailSender;
	
	
	BCryptPasswordEncoder Encoder=new BCryptPasswordEncoder(12);
	
	@RequestMapping(value= "/signup", method=RequestMethod.GET)
	public ModelAndView DisplaySignup(ModelAndView modelandview,User user) {
		modelandview.addObject("User",user);
		modelandview.setViewName("signup");
		return modelandview;
		
	}
	
	
	@RequestMapping(value= "/signup",method=RequestMethod.POST)
	public ModelAndView Register(ModelAndView modelandview,User user) {
		User existingUser=userRepository.findByEmail(user.getEmail());
		if(existingUser!=null) {
			modelandview.addObject("message","this email already register");
			modelandview.setViewName("error");
		}
			else {
				user.setPassword(Encoder.encode(user.getPassword()));
				userRepository.save(user);
				modelandview.addObject("message","sucessful register");
				ConfirmationToken confirmationToken=new ConfirmationToken(user);
				
				confirmationTokenRepository.save(confirmationToken);
				
				SimpleMailMessage mailMessage=new SimpleMailMessage();
				mailMessage.setTo(user.getEmail());
				mailMessage.setSubject("complete registration");
				mailMessage.setFrom("testvinoth45@gmail.com");
				mailMessage.setText("To complete the password reset process, please click here: "
						+"http://localhost:8090/confirm-reset?token="+confirmationToken.getConfirmationToken());
						
				emailSender.sendEmail(mailMessage);
				//modelandview.addObject("email",user.getEmail());
				//modelandview.setViewName("successfulRegisteration");
			}
				return modelandview;	
			}
	@RequestMapping(value= "/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token")String confirmationToken) {
		ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
		
		if(token != null)
		{
			User user = userRepository.findByEmail(token.getUser().getEmail());
			user.setEnabled(true);
			userRepository.save(user);
			modelAndView.setViewName("accountVerified");
		}
		else
		{
			modelAndView.addObject("message","The link is invalid or broken!");
			modelAndView.setViewName("error");
		}
		
		return modelAndView;
	}	

			
		@RequestMapping(value= "/",method=RequestMethod.GET)
		public ModelAndView loginPage(ModelAndView modelandview,User user) {
			modelandview.addObject("User",user);
			modelandview.setViewName("login");
			return modelandview;
		}
		
		@RequestMapping(value= "/",method=RequestMethod.POST)
		public ModelAndView loginpage(ModelAndView modelandview,User user) {
			User existingUser=userRepository.findByEmail(user.getEmail());
			if(existingUser!=null) {
				if(Encoder.matches(user.getPassword(), existingUser.getPassword())) {
					modelandview.addObject("message","welcome");
					modelandview.setViewName("home");
				}
				else {
					modelandview.addObject("message","incorrect password");
					modelandview.setViewName("login");
				}
			}
				else {
					modelandview.addObject("message","not exist");
					modelandview.setViewName("login");
				}
			return modelandview;
		}
		@RequestMapping(value= "/forgotpassword",method=RequestMethod.GET)
		public ModelAndView forgotpass(ModelAndView modelandview,User user) {
			modelandview.addObject("User",user);
			modelandview.setViewName("forgotpassword");
			return modelandview;
		}
		@RequestMapping(value= "/forgotpassword",method=RequestMethod.POST)
		public ModelAndView forgotPass(ModelAndView modelandview,User user) {
			User existingUser=userRepository.findByEmail(user.getEmail());
			if(existingUser!=null) {
ConfirmationToken confirmationToken=new ConfirmationToken(existingUser);
				
				confirmationTokenRepository.save(confirmationToken);
				
				SimpleMailMessage mailMessage=new SimpleMailMessage();
				mailMessage.setTo(user.getEmail());
				mailMessage.setSubject("complete registration");
				mailMessage.setFrom("testvinoth45@gmail.com");
				mailMessage.setText("To complete the password reset process, please click here: "
						+"http://localhost:8090/confirm-reset?token="+confirmationToken.getConfirmationToken());
						
				emailSender.sendEmail(mailMessage);
				modelandview.addObject("message","Request to reset password received. Check your inbox for the reset link.");
	            modelandview.setViewName("successForgotPassword");		
			} else {	
				modelandview.addObject("message", "This email does not exist!");
				modelandview.setViewName("error");
			}
			
			return modelandview;
		}
		
		
		@RequestMapping(value= "/confirm-reset", method= {RequestMethod.GET, RequestMethod.POST})
		public ModelAndView validateResetToken(ModelAndView modelAndView, @RequestParam("token")String confirmationToken)
		{
			ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
			
			if(token != null) {
				User user = userRepository.findByEmail(token.getUser().getEmail());
				user.setEnabled(true);
				userRepository.save(user);
				modelAndView.addObject("user", user);
				modelAndView.addObject("emailId", user.getEmail());
				modelAndView.setViewName("resetPassword");
			} else {
				modelAndView.addObject("message", "The link is invalid or broken!");
				modelAndView.setViewName("error");
			}
			
			return modelAndView;
}
		
		
		@RequestMapping(value = "/reset-password", method = RequestMethod.POST)
		public ModelAndView resetUserPassword(ModelAndView modelAndView, User user) {
			// ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
			
			if(user.getEmail() != null) {
				// use email to find user
				User tokenUser = userRepository.findByEmail(user.getEmail());
				tokenUser.setEnabled(true);
				tokenUser.setPassword(Encoder.encode(user.getPassword()));
				// System.out.println(tokenUser.getPassword());
				userRepository.save(tokenUser);
				modelAndView.addObject("message", "Password successfully reset. You can now log in with the new credentials.");
				modelAndView.setViewName("successResetPassword");
			} else {
				modelAndView.addObject("message","The link is invalid or broken!");
				modelAndView.setViewName("error");
			}
			
			return modelAndView;
		}
		@RequestMapping(value="/logout",method=RequestMethod.GET)
		public ModelAndView logiut(ModelAndView modelandview) {
			modelandview.setViewName("/");
			return modelandview;
		}
		
		
		 @RequestMapping(value="/home/home", method=RequestMethod.GET)
		 public ModelAndView home(ModelAndView modelandview) {
			 modelandview.setViewName("home");
			 return modelandview;
		 }
		 @RequestMapping(value="/add",method=RequestMethod.GET)
		 public ModelAndView ad(ModelAndView modelandview) {
			 modelandview.setViewName("/view");
			 return modelandview;
		 }
		 
		 
		
		 


		public UserRepository getUserRepository() {
			return userRepository;
		}


		public void setUserRepository(UserRepository userRepository) {
			this.userRepository = userRepository;
		}


		public ConfirmationTokenRepository getConfirmationTokenRepository() {
			return confirmationTokenRepository;
		}


		public void setConfirmationTokenRepository(ConfirmationTokenRepository confirmationTokenRepository) {
			this.confirmationTokenRepository = confirmationTokenRepository;
		}


		public EmailSenter getEmailSender() {
			return emailSender;
		}


		public void setEmailSender(EmailSenter emailSender) {
			this.emailSender = emailSender;
		}
		

}
		
		

		
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
			
	
	
	
	

