/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.system;

import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Colin Halseth
 */
public class StateMachine {
 
    //Internal classes
    protected class StateCondition{
        protected String var;
        protected String value;
        protected String comparator;
    }
    
    protected class StateTransition{
        protected LinkedList<StateCondition> conditions = new LinkedList<StateCondition>();
        
        protected void AddCondition(String s){
            String[] splits = s.split("[><=]");
            StateCondition cond = new StateCondition();
            cond.var = splits[0].trim();
            cond.value = splits[1].trim();
            cond.comparator = s.contains("=")?"=":(s.contains(">")?">":(s.contains("<")?"<":"="));
            conditions.add(cond);
        }
        
        public boolean CheckTransition(HashMap<String,String> vars){
            for(StateCondition cond : this.conditions){
                if(vars.containsKey(cond.var)){
                    String val = vars.get(cond.var);
                    switch(cond.comparator){
                        case ">":
                            if(Double.parseDouble(val) <= Double.parseDouble(cond.value)){
                                return false;
                            }
                            break;
                        case "<":
                            if(Double.parseDouble(val) >= Double.parseDouble(cond.value)){
                                return false;
                            }
                            break;
                        case "=":
                        default:
                            if(!val.equals(cond.value)){
                                return false;
                            }
                            break;
                    }
                }else{
                    return false;
                }
            }
            
            return true;
        }
    }
    
    protected class State{
       protected String name;
       protected HashMap<String,StateTransition> trans = new HashMap<String,StateTransition>();
       public State(String name){this.name = name;}
       
    }
    
    protected HashMap<String,String> vars = new HashMap<String,String>();
    protected HashMap<String,State> states = new HashMap<String,State>();
    protected State currentState;
    
    /**
     * Create a new state machine starting in the root state
     * @param root 
     */
    public StateMachine(String root){
        AddState(root);
        currentState = states.get(root);
    }
    
    /**
     * Add a state to this state machine
     * @param name 
     */
    public void AddState(String name){
        states.put(name, new State(name));
    }
    
    /**
     * Remove a state from this state machine
     * @param name 
     */
    public void RemoveState(String name){
        states.remove(name);
    }
    
    /**
     * Set the current state of this machine
     * @param state 
     */
    public void SetState(String state){
        currentState = states.get(state);
    }
    
    /**
     * Gets the state that is currently active
     * @return name of state
     */
    public String GetState(){
        return currentState.name;
    }
    
    /**
     * Create a condition to transition from one state to another
     * @param fromstate. The state that you start at.
     * @param tostate. The state the transition ends on.
     * @param ext. The expression that, if true causes fromstate to transition to tostate. 
     */
    public void AddTransition(String fromstate, String tostate, String ext){
        //Get the start state
        State state = states.get(fromstate);
        //Grab transition set from fromstate to tostate, create one is the set doesnt already exist
        StateTransition transition;
        if(state.trans.containsKey(tostate)){
            transition = state.trans.get(tostate);
        }else{
            transition = new StateTransition();
            state.trans.put(tostate, transition);
        }
        //Add the ext to that transition
        transition.AddCondition(ext);
    }
    
    /**
     * Remove all transition linkages from fromstate to tostate
     * @param fromstate
     * @param tostate 
     */
    public void RemoveTransition(String fromstate, String tostate){
        State state = states.get(fromstate);
        state.trans.remove(tostate);
    }
    
    /**
     * Get all available transitions for the active state
     * @return 
     */
    public String[] GetTransitions(){
        String[] trans = new String[currentState.trans.keySet().size()];
        int i = 0;
        for(String state : currentState.trans.keySet()){
            trans[i++] = state;
        }
        return trans;
    }
    
    /**
     * Get all values of all variables
     * @return 
     */
    public String GetAllValues(){
        String vals = "";
        for(String state : this.vars.keySet()){
            vals += state+": "+this.vars.get(state)+"\n";
        }
        return vals;
    }
    
    /**
     * Performs a step in state machine logic. If transitions from the current state exist they will be evaluated and the first set of state that is true will become the new current state.
     */
    public void Step(){
        for(String state : currentState.trans.keySet()){
            StateTransition transition = currentState.trans.get(state);
            boolean trans = transition.CheckTransition(vars);
            if(trans){
                this.currentState = states.get(state);
                return;
            }
        }
    }
    
    /**
     * Set a value in this statemachine
     * @param name
     * @param value 
     */
    public void SetValue(String name, String value){
        vars.put(name, value);
    }
    
    /**
     * Set a value in this state machine
     * @param name
     * @return 
     */
    public String GetValue(String name){
        return vars.get(name);
    }
    
    /**
     * Remove a value from this state machine
     * @param name 
     */
    public void ClearValue(String name){
        vars.remove(name);
    }
    
    public String toString(){
        String beliefs = "";
        for(String k : this.vars.keySet()){
            beliefs += (k+":"+this.vars.get(k)+", ");
        }
        String transitions = "";
        for(String k : currentState.trans.keySet()){
            transitions += k+", ";
        }
        return currentState.name+" ("+beliefs+") -> ("+transitions;
    }
}
