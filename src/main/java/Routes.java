import java.net.URI;

public final class Routes {

    // pages
    //public String loginPage = "https://news-feed.dunice-testing.com/loign" + System.getenv("INTERNSHIP_NGROK");
    //public String loginPage = "https://news-feed.dunice-testing.com/api";

    public String registrationPage = "https://news-feed.dunice-testing.com/registration";

    // requests
    public String getNews = "https://news-feed.dunice-testing.com/api/v1/news?page=1&perPage=3";

    public String postLogin = "https://news-feed.dunice-testing.com/api/v1/auth/login";

    public String postRegistration = "https://news-feed.dunice-testing.com/api/v1/auth/register";
    public String userAllInfo = "https://news-feed.dunice-testing.com/api/v1/user/";

    public String deleteUser = "https://news-feed.dunice-testing.com/api/v1/user";

    public String updateUser = "https://news-feed.dunice-testing.com/api/v1/user";

    public String paginationNews = "https://news-feed.dunice-testing.com/api/v1/news";

    public String createNews = "https://news-feed.dunice-testing.com/api/v1/news";

    public String deleteNews = "https://news-feed.dunice-testing.com/api/v1/news";

    public String searchOneNews = "https://news-feed.dunice-testing.com/api/v1/news/find";
    public String fileUpload = "https://news-feed.dunice-testing.com/api/v1/file/uploadFile";

    public String getFile = "https://news-feed.dunice-testing.com/api/v1/file/";

    public String updateNews = "https://news-feed.dunice-testing.com/api/v1/news";
}
