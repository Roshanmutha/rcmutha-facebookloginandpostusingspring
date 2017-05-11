package hello;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.Page;
import org.springframework.social.facebook.api.PagePostData;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LoginAndPostController {

	private Facebook facebook;
	private ConnectionRepository connectionRepository;

	public LoginAndPostController(Facebook facebook, ConnectionRepository connectionRepository) {
		this.facebook = facebook;
		this.connectionRepository = connectionRepository;
	}

	@GetMapping
	public String loginAndPostOnPage(Model model) {
		if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
			return "redirect:/connect/facebook";
		}

		// model.addAttribute("facebookProfile",
		// facebook.userOperations().getUserProfile());
		System.out.println(this.facebook.isAuthorized());
		// make sure you add right permissions in facebookconnect.html. <input
		// type="hidden" name="scope"
		// value="publish_pages,manage_pages,user_posts" />. Change
		// 'punerifashion'with page where you are admin
		// Post on page where user is admin.
		Page page = facebook.pageOperations().getPage("punerifashion");
		System.out.println("page.getId()" + page.getId());
		final PagePostData post = new PagePostData(page.getId());
		post.message("First Spring Social post" + Math.random());
		post.link("https://spring.io/guides/gs/accessing-facebook/", null, null, null, null);
		this.facebook.pageOperations().post(post);
		PagedList<Post> feed = facebook.feedOperations().getFeed();
		model.addAttribute("feed", feed);
		return "hello";
	}

}
