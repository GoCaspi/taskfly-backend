package com.gocaspi.taskfly.task;

import org.bson.types.ObjectId;

public class Task {
    private String userId;
    private String listId;
    private String team;
    private String deadline;
    private ObjectId id;
    private String taskId;
    private Taskbody body;
   public static class Taskbody {
        private String topic;
        private String priority;
        private String description;

        public Taskbody(String topic, String priority,String description){
            this.topic = topic;
            this.priority = priority;
            this.description = description;
        }
       /**
        * sets the priority of a task to a new priority
        *
        * @param priority, new value of the task-field: priority
        */
        public void setPriority(String priority){
            this.priority = priority;
        }
       /**
        * sets the description of a task to a new description
        *
        * @param description, new value of the task-field: description
        */
        public void setDescription(String description){
            this.description = description;
        }
       /**
        * sets the topic of a task to a new topic
        *
        * @param topic, new value of the task-field: topic
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
        * returns the priority of the task
        *
        * @return String, priority of the task
        */
        public String getPriority(){
            return this.priority;
        }

    }

    public Task(String userId, String listId, String team,  String deadline, ObjectId id, Taskbody body){
        this.userId = userId;
        this.listId = listId;
        this.team = team;
        this.deadline = deadline;
        this.id = id;
        this.taskId = id.toString();
        this.body = body;
    }
    public Task(){
    }
    public void setBody(Taskbody body){
        this.body = body;
    }
    public Taskbody getBody(){
        return this.body;
    }


    /**
     * returns the id string of the ObjectId of the task
     *
     * @return String, id string of the ObjectId _id
     */
    public String getTaskId(){
        return this.taskId;
    }

    /**
     * sets the description of the task to a provided string (text)
     *
     * @param text, new description of the task
     */
    public void setTaskId(String text){
        this.taskId = text;
    }

    /**
     * returns the id string of the ObjectId of the task
     *
     * @return String, id string of the ObjectId _id
     */
    public String getTaskIdString(){
        return this.id.toString();
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
    public void setDeadline(String deadline){
        this.deadline = deadline;
    }

    /**
     * returns the deadline of the task
     *
     * @return String, deadline of the task
     */
    public String getDeadline(){ return this.deadline; }


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
