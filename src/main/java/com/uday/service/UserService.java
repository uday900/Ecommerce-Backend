package com.uday.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.uday.Dto.CartDto;
import com.uday.Dto.CartItemDTO;
import com.uday.Dto.LoginRequest;
import com.uday.Dto.Response;
import com.uday.Dto.UserDto;
import com.uday.entity.*;
import com.uday.enums.UserRole;
import com.uday.exception_handling.NotFoundException;
import com.uday.mapper.EntityAndDtoMapper;
import com.uday.repository.*;
import com.uday.security.JWTService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final EntityAndDtoMapper entiyAndDtoMapper;
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private TokenBlacklistService tokenBlacklistService;
	
	@Value("${frontend.url}")
	private String frontEndUrl;

	// reset password
	public Response resetPassword(String email, String newPassword, String token) {

		User user = userRepository.findByEmail(email);
		if (user == null) throw new NotFoundException("user not found with "+ email);
//			return Response.builder().status(404).message("user not found").build();
//		}
		
		System.out.println(user.getResetPasswordToken() + " " + token);
		System.out.println(user.getResetPasswordExpires() + " " + LocalDateTime.now());
		if (
				user.getResetPasswordToken().equals(token) && 
				user.getResetPasswordExpires().isAfter(LocalDateTime.now())
			) {
			user.setPassword(new BCryptPasswordEncoder(12).encode(newPassword));
			user.setResetPasswordToken(null);
			user.setResetPasswordExpires(null);
			
			// new BCryptPasswordEncoder(12).encode(userDto.getPassword())
			userRepository.save(user);
			
			return Response.builder().status(200).message("password updated succesfully").build();
		}

		throw new RuntimeException("token miss match/expires"); //.builder().status(404).message("token miss match/expires").build();
	}

	// forgot password
	public Response forgotPassword(String email) {
//		System.out.println("this is user mail " + email);

		User user = userRepository.findByEmail(email);
//		System.out.println(user);
		if (user == null) {
			throw new NotFoundException("user not found with "+email);
		}

		// generate a token and save it in db
//		String DIGITS = "0123456789";
		StringBuilder otp = new StringBuilder();
		Random random = new Random();
		
		for (int i = 0; i < 6; i++) otp.append( random.nextInt(9));
		
		
		String token = otp.toString(); //UUID.randomUUID().toString();
		
		user.setResetPasswordToken(token);
		user.setResetPasswordExpires(LocalDateTime.now().plusMinutes(5));

		System.out.println("saved token in db");
		userRepository.save(user);
		System.out.println("saved token in db");

		String content = "Your OTP Code: " + otp
				+ "\nUse the following One-Time Password (OTP) to verify your account: " + otp + ""
				+ "\nThis OTP is valid for only 5 minutes. Do not share it with anyone."
				+ "\nIf you did not request this OTP, please ignore this email."
				+ "\n\nBest Regards,\nDarla Dass";
		

		
		
		String body = "Resent your password. " + "Link is active for 5 min only " + frontEndUrl +"/reset-password" + "?token=" + token
				+ "&username=" + email;
		String subject = "Your OTP Code";

		emailService.sendEmail(email, subject, content);

		return Response.builder().status(200).message("OTP sent to " + email).build();
	}

	// remove item from cart
	public Response removeCartItem(Long userId, Long cartItemId) {

		// check if user exists
		Cart cart = cartRepository.findByUserId(userId);
		if (cart != null) {

			List<CartItem> cartItems = cart.getCartItems();
			Optional<CartItem> cartItem = cartItems.stream().filter(item -> item.getId().equals(cartItemId))
					.findFirst();

			if (cartItem.isPresent()) {
				CartItem existCartItem = cartItem.get();
				System.out.println("item is present " + cartItemId);

				// Remove item from cart's list
				cartItems.remove(existCartItem);

//				cartItemRepository.delete(existCartItem);
				cartItemRepository.deleteById(cartItemId);

				cart.setCartItems(cartItems);
				cartRepository.save(cart);

				return Response.builder().status(200)

						.message("item delete").build();
			}

		}

		return Response.builder().status(404).message("user/item not found").build();
	}

	// get all users
	public Response getAllUsers() {

		List<UserDto> users = userRepository.findAll().stream().map(entiyAndDtoMapper::mapToUserDto)
				.collect(Collectors.toList());
		// exclude admin
		users = users.stream().filter(user -> user.getRole() != UserRole.ADMIN).collect(Collectors.toList());
		
		return Response.builder().status(200).users(users).build();

	}

	// get user by id
	public Response getUserById(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException("user not found with id "+userId));
//				.orElse(null);
		
//		User = user.get();
//		if (user == null) {
//			return Response.builder().status(400).message("User not found").build();
//		}

		UserDto userDto = UserDto.builder()
				.id(user.getId())
				.email(user.getEmail())
				.name(user.getName())
				.phoneNumber(user.getPhoneNumber())
				.address(user.getAddress())
//				.password(user.getPassword())

				.build();
		return Response.builder().status(200).message("User found").user(userDto).build();

	}

	// get user cart items
	public Response getUserCart(Long userId) {

		// check if there is a cart for user
		Cart cart = cartRepository.findByUserId(userId);
		if (cart == null) {
			// Handle the case where no cart exists for the user
			return Response.builder().status(404).message("Cart not found").build();
		}

		List<CartItemDTO> cartItems = cart.getCartItems().stream()
				.map(item -> CartItemDTO.builder().id(item.getId()).productId(item.getProduct().getId())
						.ProductName(item.getProduct().getName()).price(item.getProduct().getPrice())
						.imageData(item.getProduct().getImageData()).imageUrl(item.getProduct().getImageUrl())
						.quantity(item.getQuantity()).build())
				.collect(Collectors.toList());

		Double totalAmount = cart.getCartItems().stream()
				.mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice()).sum();

		// building cartDto
		CartDto cartDto = CartDto.builder().id(cart.getId()).userId(userId).totalAmount(totalAmount)
				.cartItems(cartItems)

				.build();

		return Response.builder().status(200).message("User cart items").cart(cartDto).build();
	}

	// add product to cart
	// say cartId comes as param
	public Response addItemtoCart(Long userId, Long productId) {

		Cart cart = cartRepository.findByUserId(userId);
//				findById(cartId).orElse(null);

		Product product = productRepository.findById(productId).orElse(null);

		if (cart == null || product == null) {
			return Response.builder().status(400).message("Cart/product not found").build();

		}
		System.out.println("cart is present");

		// check if there exists product if -> increment quantity
		List<CartItem> existingCartItems = cart.getCartItems();

		Optional<CartItem> existingItem = existingCartItems.stream()
				.filter(item -> item.getProduct().getId().equals(productId)).findFirst();

		if (existingItem.isPresent()) {
			CartItem item = existingItem.get();
			item.setQuantity(item.getQuantity() + 1);
//			item.setQuantity(1);

			cartItemRepository.save(item);
		} else {
			CartItem cartItem = CartItem.builder().cart(cart).product(product)
//					.quantity(cartItemDto.getQuantity())
					.quantity(1).build();

			cartItemRepository.save(cartItem);

			existingCartItems.add(cartItem);
			cart.setCartItems(existingCartItems);
		}

		// saving cart
		cartRepository.save(cart);

		System.out.println("added item");
		return Response.builder().status(200).message("Item added into cart success").build();
	}

	// register user
	public Response registerUser(UserDto userDto) {

		System.out.println("registering user");
		User user = userRepository.findByEmail(userDto.getEmail());
		
//		System.out.println(user);
		
		if (user != null) {
			return Response.builder().status(400).message("User already exists").build();
		}
//		user = entiyAndDtoMapper.mapToUser(userDto);

		User newUser = User.builder()
				.name(userDto.getName())
				.email(userDto.getEmail())
				.password(
						new BCryptPasswordEncoder(12).encode(userDto.getPassword())
						)
				.address(userDto.getAddress())
				.phoneNumber(userDto.getPhoneNumber())
				
				.build();

		newUser.setRole(UserRole.USER);

		userRepository.save(newUser);

		// creating an empty cart for user
		Cart cart = Cart.builder().user(newUser).totalAmount(0.0).build();

		cartRepository.save(cart);
		System.out.println("an empty cart created for user");

		// set user address

		return Response.builder()
				.status(200)
				.message("User registered successfully")
				.build();
	}

	public Response getCartCount(Long userId) {
		// TODO Auto-generated method stub
		Cart cart = cartRepository.findByUserId(userId);
		if (cart == null) {
			return Response.builder().status(400).message("user not found").build();
		}

		return Response.builder().status(200).cartCount(cart.getCartItems().size()).build();
	}

	public Response incrementQuantity(Long userId, Long cartItemId) {
		// TODO Auto-generated method stub
		Cart cart = cartRepository.findByUserId(userId);
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);
		if (cart == null || cartItem == null) {
			return Response.builder().status(400).message("user/item not found").build();
		}

//		cartItemRepository.deleteById(cartItemId);
		cartItem.setQuantity(cartItem.getQuantity() + 1);

//		cart.setCartItems(null)
		cartItemRepository.save(cartItem);
//		cartRepository.save(cart);

		return Response.builder().status(200).message("incremented").build();
	}

	public Response decrementQuantity(Long userId, Long cartItemId) {
		// TODO Auto-generated method stub
		Cart cart = cartRepository.findByUserId(userId);
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);
		if (cart == null || cartItem == null) {
			return Response.builder().status(400).message("user/item not found").build();
		}

//		cartItemRepository.deleteById(cartItemId);
		cartItem.setQuantity(cartItem.getQuantity() - 1);

//		cart.setCartItems(null)
		cartItemRepository.save(cartItem);
//		cartRepository.save(cart);

		return Response.builder().status(200).message("decremented").build();
	}

	public Response authenticateUser(LoginRequest loginRequest) {

		System.out.println("validating user............ " + loginRequest.getEmail());

		User user = userRepository.findByEmail(loginRequest.getEmail());
		if (user == null) throw new NotFoundException("user not found with "+loginRequest.getEmail());
		
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		

		if (authentication.isAuthenticated()) {

			// if user authenticated generate a JWT token
			String token = jwtService.generateToken(loginRequest.getEmail());

			if (token != null) {
				
				UserDto userDto = UserDto.builder()
						.id(user.getId())
						.email(user.getEmail())
						.name(user.getName())
						.role(user.getRole())
						
						.phoneNumber(user.getPhoneNumber())
						.address(user.getAddress())
//						.password(user.getPassword())

						.build();
				
				System.out.println("verified user");
				
				// set authentication in security context
				SecurityContextHolder.getContext().setAuthentication(authentication);
				return Response.builder()
						.status(200)
						.token(token)
						.user(userDto)
						
						.message("Login successfully")
						.build();
			}
			
//			new ResponseEntity<>("failed to generated token", HttpStatus.BAD_REQUEST);
		}


		return Response.builder()
				.status(400)
				.message("user not found/invalid credentials")
				.build();
		
	}

	public Response verifyAccount(String token) {
		
		// check the token is in black list 
		System.out.println("verifcation ..");
		
		String username = jwtService.extractUserName(token);
		
		User user = userRepository.findByEmail(username);
		if (user == null) {
			throw new NotFoundException("user not found with " + username);
		}
		
		
		if (tokenBlacklistService.isBlacklisted(token) || jwtService.isTokenExpire(token)) {
			System.out.println("Invalidate bro");
        	throw new RuntimeException("Token has been invalidated/expires.");
        }
//		
//		if (jwtService.isTokenExpire(token)) {
//			throw new RuntimeException("token expired");
//        }
		
		return Response.builder().status(200).message("account verified").build();
	}

	public Response verifyOtp(String otp, String email) {
		
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new NotFoundException("user not found with " + email);
		}
		
		// check if otp is valid
		if (user.getResetPasswordExpires().isAfter(LocalDateTime.now()) &&
				user.getResetPasswordToken().equals(otp)) {
			
			// set new token and expires
			String token = UUID.randomUUID().toString();
			user.setResetPasswordToken(token);
			user.setResetPasswordExpires(LocalDateTime.now().plusMinutes(5));
			
			userRepository.save(user);
			
			return Response.builder()
					.status(200)
					.message("Verified OTP successfully, now you can reset your password within 5 minutes")
					.token(token)
					.build();
		}
		
		return Response.builder().status(400).message("OTP miss match/expired").build();
	}

}
