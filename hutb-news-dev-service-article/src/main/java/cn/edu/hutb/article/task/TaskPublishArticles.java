package cn.edu.hutb.article.task;


import cn.edu.hutb.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration      // 标记配置类，使得springboot容器扫描到
@EnableScheduling   // 开启定时任务
public class TaskPublishArticles {

    @Autowired
    private ArticleService articleService;

    /**
     * 添加定时任务，注明定时任务的表达式
     */
    @Scheduled(cron = "0/3 * * * * ?")
    private void publishArticles() {
        // 调用文章service，把当前时间应该发布的定时文章，状态改为即时
        articleService.updateAppointToPublish();
    }

}
