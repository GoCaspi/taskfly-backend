package com.gocaspi.taskfly.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;



public class Task {
    @NotBlank
    private String userId;
    @NotBlank
    private String listId;
    private String team;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime deadline;
    @Id
    private ObjectId id;
    private Taskbody body;
   public static class Taskbody {
        private String topic;
        private Boolean highPriority;
        private String description;

        public Taskbody(String topic, Boolean priority,String description){
            this.topic = topic;
            this.highPriority = priority;
            this.description = description;
        }

        public void setHighPriority(Boolean str){
            this.highPriority = str;
        }
        public void setDescription(String str){
            this.description = str;
        }
        public void setTopic(String str){
            this.topic = str;
        }

        public String getTopic(){
            return this.topic;
        }
        public String getDescription(){
            return this.description;
        }
        public Boolean getHighPriority(){
            return this.highPriority;
        }



    }



    public Task(String userId, String listId, String team, LocalDateTime deadline, ObjectId id, Taskbody body){
        this.userId = userId;
        this.listId = listId;
        this.team = team;
        this.deadline = deadline;
        this.id = id;
        this.body = body;
    }


    public void setBody(Taskbody body){
        this.body = body;
    }
    public Taskbody getBody(){
        return this.body;
    }


    /**
     * sets the userIDs array of a task to the provided String-array (newUserIds)
     *
     * @param newUserId, new UserIds array of the task
     */
    public void setUserId(String newUserId){
        this.userId = newUserId;
    }

    /**
     * returns an array of user ids that are assigned to that task
     *
     * @return String[], userIds that are assigned to that task
     */
    public String getUserId(){ return this.userId; }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    /**
     * sets the team of a task to a new team (team)
     *
     * @param team, new value of the task-field: team
     */
    public void setTeam(String team){
        this.team = team;
    }

    /**
     * returns the team of the task
     *
     * @return String, team of the task
     */
    public String getTeam(){ return this.team; }

    /**
     * sets the deadline of a task to a new deadline
     *
     * @param deadline, new value of task-field: deadline
     */
    public void setDeadline(LocalDateTime deadline){
        this.deadline = deadline;
    }

    /**
     * returns the deadline of the task
     *
     * @return String, deadline of the task
     */
    public LocalDateTime getDeadline(){ return this.deadline; }


    /**
     * sets the value of the listId of the task to the given input
     *
     * @param newListId, String new listId of the task
     */
    public void setListId(String newListId){ this.listId = newListId; }

    /**
     * returns the value of the listId of the task (id of the list that contains this task)
     *
     * @return String, listId of the task
     */
    public String getListId(){ return this.listId; }
}
