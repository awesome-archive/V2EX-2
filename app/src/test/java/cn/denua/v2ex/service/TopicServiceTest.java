/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import cn.denua.v2ex.api.TopicApi;
import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.http.RxObserver;
import cn.denua.v2ex.interfaces.IResponsibleView;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Reply;
import cn.denua.v2ex.model.Tag;
import cn.denua.v2ex.model.Topic;

public class TopicServiceTest {

    @Before
    public void init(){

        RetrofitManager.init(null);
    }

    @Test
    public void getHot() {

        RetrofitManager.init(null);

        RetrofitManager.create(TopicApi.class)
                .getHotTopic()
                .subscribe(new RxObserver<JsonArray>() {
                    @Override
                    public void _onNext(JsonArray jsonElements) {
                        jsonElements.forEach(jsonElement -> {
                            Topic topic = new Gson().fromJson(jsonElement, Topic.class);
                            System.out.println(topic.getTitle());
                            System.out.print(topic.getReplies());
                            System.out.print(topic.getReplies());
                            System.out.print(topic.getContent());
                        });
                    }
                    @Override
                    public void _onError(String msg) {
                        System.err.println(msg);
                    }
                });
    }

    @Test
    public void getReply(){

        Topic topic = new Topic();
        topic.setId(505378);

        new TopicService<>(new IResponsibleView() {
            @Override
            public void onStartRequest() { }

            @Override
            public void onProcessData(int progress) { }

            @Override
            public void onCompleteRequest() { }

            @Override
            public int getContextStatus() {
                return IResponsibleView.VIEW_STATUS_ACTIVATED;
            }
        }, new ResponseListener<List<Topic>>() {
            @Override
            public void onComplete(List<Topic> result) {
                Topic topic1 = result.get(0);
                List<Reply> replies = topic1.getReplyList();

                Log.d("CsrfToken", topic1.getCsrfToken());
                Log.d("Node:", topic1.getNode().getName() + topic1.getNode().getTitle());
                Log.d("Thanks:", String.valueOf(topic1.getThanks()));
                Log.d("Clicks:", String.valueOf(topic1.getClicks()));

                for (Tag tag:topic1.getTags()){
                    Log.d("Tag", tag.getName());
                }

                if (replies == null){
                    Log.e(null, "No replies exists.");
                    return;
                }
                for (Reply reply:replies){
                    Log.d(null,  reply.getMember().getAvatar_normal());
                    Log.d(null, reply.getMember().getUsername() + reply.getAgo() + reply.getId() + reply.getVia());
                    Log.d(null, reply.getContent());
                    Log.d(null, "================================================================");
                }
            }
            @Override
            public void onFailed(String msg) {
                Log.d(null, msg);
            }
        })
        .getReply(topic, 1);
    }
    static class Log{
        static void d(String t, String s){
            System.out.println((t == null? "" : (t+"\t")) +s);
        }
        static void e(String t, String s){
            System.err.println(s);
        }
    }
}