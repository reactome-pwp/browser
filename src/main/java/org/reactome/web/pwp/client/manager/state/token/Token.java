package org.reactome.web.pwp.client.manager.state.token;

import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.manager.state.StateKey;

import java.util.*;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class Token {

    public static Long DEFAULT_SPECIES_ID; // Default in Reactome is Homo sapiens (48887) -> to be set in Browser.java
    public static String DELIMITER;        // Default in Reactome is "&" -> to be set in Browser.java

    private Map<StateKey, String> parameters;
    private boolean wellFormed = true;

    public Token(String token) throws TokenMalformedException {
        if(DEFAULT_SPECIES_ID==null || DELIMITER==null) throw new RuntimeException("Please initialise DEFAULT_SPECIES_ID and DELIMITER");
        if(token.startsWith("/"))  token = token.substring(1);
//        if(token.isEmpty()) token = StateKey.SPECIES.getDefaultKey() + "=" + DEFAULT_SPECIES_ID;

        this.parameters = new HashMap<>();
        try{
            @SuppressWarnings("NonJREEmulationClassesInClientCode")
            String[] tokens = token.split(DELIMITER);
            for (String t : tokens) {
                t = t.trim();
                if(!t.isEmpty()) {
                    if (isSingleIdentifier(t)){
                        t = t.contains(".") ? t.split("\\.")[0] : t;
                        parameters.put(StateKey.PATHWAY, t);
                    } else {
                        @SuppressWarnings("NonJREEmulationClassesInClientCode")
                        String[] ts = t.split("=", 2);
                        StateKey key = StateKey.getAdvancedStateKey(ts[0]);
                        if (key != null) {
                            parameters.put(key, ts[1]);
                        } else {
                            wellFormed = false;
                        }
                    }
                }
            }
            if(!parameters.containsKey(StateKey.SPECIES) && !parameters.containsKey(StateKey.PATHWAY)){
                this.parameters.put(StateKey.SPECIES, DEFAULT_SPECIES_ID.toString());
            }
        }catch (Exception e){
            Console.error(e.getLocalizedMessage());
            throw new TokenMalformedException();
        }
    }

    public Map<StateKey, String> getParameters() {
        return this.parameters;
    }

    public List<String> getToLoad(){
        List<String> rtn = new LinkedList<>();
        for (StateKey key : parameters.keySet()) {
            String identifier = parameters.get(key);
            switch (key) {
                case SPECIES:
                case PATHWAY:
                case SELECTED:
                    rtn.add(identifier);
                    break;
                case PATH:
                    rtn.addAll(Arrays.asList(identifier.split(",")));
                    break;
            }
        }
        return rtn;
    }

    public boolean isWellFormed() {
        return wellFormed;
    }

    public static boolean isSingleIdentifier(String identifier){
        return identifier.matches("^R\\-[A-Z]{3}\\-\\d+(\\-\\d+)?(\\.\\d+)?$") || identifier.matches("^REACT_\\d+(\\.\\d+)?$") || identifier.matches("\\d+");
    }
}
