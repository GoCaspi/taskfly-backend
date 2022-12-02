package com.gocaspi.taskfly.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
/**
 * Class for Task
 */
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
    /**
     * Class for Taskbody
     */
   public static class Taskbody {
        private String topic;
        private Boolean highPriority;
        private String description;

        /**
         * Constractor for Taskbody
         *
         * @param topic topic of the task
         * @param highPriority priority of the task
         * @param description description of the task
         */
        public Taskbody(String topic, Boolean highPriority,String description){
            this.topic = topic;
            this.highPriority = highPriority;
            this.description = description;
        }
        /**
         * sets the highPriority of a task to a new highPriority
         *
         * @param highPriority, new value of task-field: highPriority
         */
        public void setHighPriority(Boolean highPriority){
            this.highPriority = highPriority;
        }
        /**
         * sets the description of a task to a new description
         *
         * @param description, new value of task-field: description
         */
        public void setDescription(String description){
            this.description = description;
        }
        /**
         * sets the topic of a task to a new topic
         *
         * @param topic, new value of task-field: topic
         */
        public void setTopic(String topic){
            this.topic = topic;
        }
        /**
         * returns the topic of the task
         *
         * @return String, topic of the task
         */
        public String getTopic(){
            return this.topic;
        }
        /**
         * returns the description of the task
         *
         * @return String, description of the task
         */
        public String getDescription(){
            return this.description;
        }
        /**
         * returns the highPriority of the task
         *
         * @return String, highPriority of the task
         */
        public Boolean getHighPriority(){
            return this.highPriority;
        }
    }
    /**
     * Constractor for Task
     * @param userId userId of the Task
     * @param listId listId of the Task
     * @param team team of the Task
     * @param deadline deadline of the Task
     * @param id id of the Task
     * @param body body for the Task
     */
    public Task(String userId, String listId, String team, LocalDateTime deadline, ObjectId id, Taskbody body){
        this.userId = userId;
        this.listId = listId;
        this.team = team;
        this.deadline = deadline;
        this.id = id;
        this.body = body;
    }

    /**
     * Empty Constractor Task for testing
     */
    public Task(){
    }

    /**
     * sets the body of a Taskbody to a new body
     *
     * @param body, new value of the Taskbody-field: body
     */
    public void setBody(Taskbody body){
        this.body = body;
    }
    /**
     * returns the body of the Taskbody
     *
     * @return String, body of the Taskbody
     */
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
    /**
     * returns the id of the task
     *
     * @return ObjectId, id of the task
     */
    public ObjectId getId() {
        return id;
    }
    /**
     * sets the id of a task to a new id
     *
     * @param id, new value of task-field: id
     */
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
     * @return LocalDateTime, deadline of the task
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
