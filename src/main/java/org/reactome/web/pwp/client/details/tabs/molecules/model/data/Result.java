package org.reactome.web.pwp.client.details.tabs.molecules.model.data;

import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.common.utils.MapSet;
import org.reactome.web.pwp.client.details.tabs.molecules.model.type.PropertyType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class Result {
    //"grouping" for different kind of molecules
    private HashSet<Molecule> chemicals = new HashSet<Molecule>();
    private HashSet<Molecule> proteins = new HashSet<Molecule>();
    private HashSet<Molecule> sequences = new HashSet<Molecule>();
    private HashSet<Molecule> drugs = new HashSet<Molecule>();
    private HashSet<Molecule> others = new HashSet<Molecule>();

    //mapping each ReferenceEntity to a PhysicalEntityID
    private MapSet<PhysicalToReferenceEntityMap, Molecule> phyEntityToRefEntitySet = new MapSet<PhysicalToReferenceEntityMap, Molecule>();

    public Result(ArrayList<Molecule> molecules){
        for(Molecule molecule : molecules){
            //SchemaClass doesn't seem to be aware of hierarchy therefore each class must be checked
            switch (molecule.getSchemaClass()){
                case ENTITY_WITH_ACCESSIONED_SEQUENCE:
                    proteins.add(molecule);
                    break;
                case REFERENCE_RNA_SEQUENCE:
                case REFERENCE_DNA_SEQUENCE:
                    sequences.add(molecule);
                    break;
                case SIMPLE_ENTITY:
                    chemicals.add(molecule);
                    break;
                case REFERENCE_THERAPEUTIC:
                    drugs.add(molecule);
                    break;
                default:
                    others.add(molecule);
            }
        }
    }

    /*getters & setters*/
    public HashSet<Molecule> getChemicals() {
        return chemicals;
    }

    public HashSet<Molecule> getProteins() {
        return proteins;
    }

    public HashSet<Molecule> getSequences() {
        return sequences;
    }

    public HashSet<Molecule> getDrugs() {
        return drugs;
    }

    public HashSet<Molecule> getOthers() {
        return others;
    }

    public MapSet<PhysicalToReferenceEntityMap, Molecule> getPhyEntityToRefEntitySet() {
        return this.phyEntityToRefEntitySet;
    }

    public void setPhyEntityToRefEntitySet(MapSet<PhysicalToReferenceEntityMap, Molecule> phyEntityToRefEntitySet) {
        this.phyEntityToRefEntitySet = phyEntityToRefEntitySet;
    }

    /**
     * Getter for sorted chemicals, molecules are sorted according to their highlighting status and then there name.
     * @param propertyType defines which kind of molecules one wants to receive
     * @return ArrayList<Molecule> with sorted chemicals
     */
    public ArrayList<Molecule> getSorted(PropertyType propertyType){
        switch (propertyType){
            case CHEMICAL_COMPOUNDS:
                return this.getSortedChemicals();
            case PROTEINS:
                return this.getSortedProteins();
            case SEQUENCES:
                return this.getSortedSequences();
            case DRUG:
                return this.getSortedDrugs();
            default:
                return this.getSortedOthers();
        }
    }

     /**
     * Getter for sorted chemicals, molecules are sorted according to their highlighting status and then there name.
     * @return ArrayList<Molecule> with sorted chemicals
     */
    private ArrayList<Molecule> getSortedChemicals(){
        ArrayList<ArrayList<Molecule>> split = splitHighlighted(this.chemicals);
        ArrayList<Molecule> sortedColour = new ArrayList<Molecule>(split.get(0));
        Collections.sort(sortedColour);
        ArrayList<Molecule> sortedGrey = new ArrayList<Molecule>(split.get(1));
        Collections.sort(sortedGrey);
        sortedColour.addAll(sortedGrey);
        return sortedColour;
    }

    /**
     * Getter for sorted proteins, molecules are sorted according to their highlighting status and then there name.
     * @return ArrayList<Molecule> with sorted proteins
     */
    private ArrayList<Molecule> getSortedProteins(){
        ArrayList<ArrayList<Molecule>> split = splitHighlighted(this.proteins);
        ArrayList<Molecule> sortedColour = new ArrayList<Molecule>(split.get(0));
        Collections.sort(sortedColour);
        ArrayList<Molecule> sortedGrey = new ArrayList<Molecule>(split.get(1));
        Collections.sort(sortedGrey);
        sortedColour.addAll(sortedGrey);
        return sortedColour;
    }

    /**
     * Getter for sorted sequences, molecules are sorted according to their highlighting status and then there name.
     * @return ArrayList<Molecule> with sorted sequences
     */
    private ArrayList<Molecule> getSortedSequences(){
        ArrayList<ArrayList<Molecule>> split = splitHighlighted(this.sequences);
        ArrayList<Molecule> sortedColour = new ArrayList<Molecule>(split.get(0));
        Collections.sort(sortedColour);
        ArrayList<Molecule> sortedGrey = new ArrayList<Molecule>(split.get(1));
        Collections.sort(sortedGrey);
        sortedColour.addAll(sortedGrey);
        return sortedColour;
    }

    /**
     * Getter for sorted sequences, molecules are sorted according to their highlighting status and then there name.
     * @return ArrayList<Molecule> with sorted sequences
     */
    private ArrayList<Molecule> getSortedDrugs(){
        ArrayList<ArrayList<Molecule>> split = splitHighlighted(this.drugs);
        ArrayList<Molecule> sortedColour = new ArrayList<Molecule>(split.get(0));
        Collections.sort(sortedColour);
        ArrayList<Molecule> sortedGrey = new ArrayList<Molecule>(split.get(1));
        Collections.sort(sortedGrey);
        sortedColour.addAll(sortedGrey);
        return sortedColour;
    }

    /**
     * Getter for sorted other molecules, molecules are sorted according to their highlighting status and then there name.
     * @return ArrayList<Molecule> with sorted other molecules
     */
    private ArrayList<Molecule> getSortedOthers(){
        ArrayList<ArrayList<Molecule>> split = splitHighlighted(this.others);
        ArrayList<Molecule> sortedColour = new ArrayList<Molecule>(split.get(0));
        Collections.sort(sortedColour);
        ArrayList<Molecule> sortedGrey = new ArrayList<Molecule>(split.get(1));
        Collections.sort(sortedGrey);
        sortedColour.addAll(sortedGrey);
        return sortedColour;
    }

    /**
     * Splitting HashSet<Molecule> into two separate lists. One contains the highlighted and one the ones that are to
     * be faded out.
     * @param molecules to be split into highlighted and faded ones.
     * @return one ArrayList containing a list of molecules to be highlighted AND a list of molecules to be faded out.
     */
    private ArrayList<ArrayList<Molecule>> splitHighlighted(HashSet<Molecule> molecules) {
        ArrayList<Molecule> colour = new ArrayList<Molecule>();
        ArrayList<Molecule> grey   = new ArrayList<Molecule>();

        for(Molecule molecule : molecules){
            if(molecule.isToHighlight()){
                colour.add(molecule);
            }else{
                grey.add(molecule);
            }
        }

        ArrayList<ArrayList<Molecule>> all = new ArrayList<ArrayList<Molecule>>();
        all.add(colour);
        all.add(grey);
        return all;
    }

    /**
     * Getter for total number of Molecules in Result.
     * @return int totalNumber
     */
    public Integer getNumberOfMolecules() {
        Integer number = 0;
        if(chemicals != null){
            number += chemicals.size();
        }

        if(proteins != null){
            number += proteins.size();
        }

        if(sequences != null){
            number += sequences.size();
        }

        if(drugs != null){
            number += drugs.size();
        }

        if(others != null){
            number += others.size();
        }
        return number;
    }

    /**
     * Method for highlighting a molecule.
     * @param molecule to be highlighted
     */
    public void highlight(Molecule molecule){
        HashSet<Molecule> current;
        molecule.setToHighlight(true);
        if(chemicals.contains(molecule)){
            current = chemicals;
            chemicals = iterate(molecule, current);
        }else if(proteins.contains(molecule)){
            current = proteins;
            proteins = iterate(molecule, current);
        }else if(sequences.contains(molecule)){
            current = sequences;
            sequences = iterate(molecule, current);
        }else if(drugs.contains(molecule)){
            current = drugs;
            drugs = iterate(molecule, current);
        }else if(others.contains(molecule)){
            current = others;
            others = iterate(molecule, current);
        }
    }

    /**
     * Finds molecule in HashSet<Molecule> and highlights it.
     * @param molecule to be found and highlighted
     * @param current search space
     * @return HashSet<Molecule>
     */
    private HashSet<Molecule> iterate(Molecule molecule, HashSet<Molecule> current){
        ArrayList<Molecule> list = new ArrayList<Molecule>(current);
        for(Molecule m : list){
            if(m.equals(molecule)){
                m.setToHighlight(true);
            }
        }
        return new HashSet<Molecule>(list);
    }

    /**
     * Method to highlight a whole Result.
     */
    public void highlight() {
        for(Molecule m : this.chemicals){
            m.setToHighlight(true);
        }

        for(Molecule m : this.proteins){
            m.setToHighlight(true);
        }

        for(Molecule m : this.sequences){
            m.setToHighlight(true);
        }

        for(Molecule m : this.drugs){
            m.setToHighlight(true);
        }

        for(Molecule m : this.others){
            m.setToHighlight(true);
        }
    }

    /**
     * Method to undo highlighting of a whole Result.
     */
    public void undoHighlighting() {
        for(Molecule m : this.chemicals){
            m.setToHighlight(false);
        }

        for(Molecule m : this.proteins){
            m.setToHighlight(false);
        }

        for(Molecule m : this.sequences){
            m.setToHighlight(false);
        }

        for(Molecule m : this.drugs){
            m.setToHighlight(false);
        }

        for(Molecule m : this.others){
            m.setToHighlight(false);
        }
    }

    /**
     * Get the number of highlighted molecules in a result.
     * @return numOfHighlightedMolecules
     */
    public Integer getNumberOfHighlightedMolecules() {
        int numOfHighlightedMolecules = 0;
        numOfHighlightedMolecules += this.getNumHighlight(PropertyType.OTHERS);
        numOfHighlightedMolecules += this.getNumHighlight(PropertyType.SEQUENCES);
        numOfHighlightedMolecules += this.getNumHighlight(PropertyType.PROTEINS);
        numOfHighlightedMolecules += this.getNumHighlight(PropertyType.DRUG);
        numOfHighlightedMolecules += this.getNumHighlight(PropertyType.CHEMICAL_COMPOUNDS);

        return numOfHighlightedMolecules;
    }

    /**
     * Get the number of highlighted molecules in one category.
     * @param category PropertyType for which the highlighted molecules should be counted.
     * @return numHighlight
     */
    public int getNumHighlight(PropertyType category) {
        int numHighlight;
        switch (category){
            case CHEMICAL_COMPOUNDS:
                numHighlight = numHighlight(this.chemicals);
                break;
            case PROTEINS:
                numHighlight = numHighlight(proteins);
                break;
            case SEQUENCES:
                numHighlight = numHighlight(sequences);
                break;
            case DRUG:
                numHighlight = numHighlight(drugs);
                break;
            case OTHERS:
                numHighlight = numHighlight(others);
                break;
            default:
                numHighlight = -1;
                Console.error("There was an additional molecules category in class Result -> getNumHighlight.");
        }
        return numHighlight;
    }

    /**
     * Get the number of highlighted molecules in a HashSet<Molecule>.
     * @param toHighlight HashSet containing Molecules that have to be highlighted
     * @return numHighlight
     */
    private int numHighlight(HashSet<Molecule> toHighlight){
        int numHighlight = 0;
        for(Molecule molecule : toHighlight){
            if(molecule.isToHighlight()){
                ++ numHighlight;
            }
        }
        return numHighlight;
    }
}
