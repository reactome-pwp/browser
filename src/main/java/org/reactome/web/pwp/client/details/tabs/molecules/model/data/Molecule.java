package org.reactome.web.pwp.client.details.tabs.molecules.model.data;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.resources.client.ImageResource;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.ReferenceEntity;
import org.reactome.web.pwp.model.client.factory.DatabaseObjectImages;
import org.reactome.web.pwp.model.client.factory.SchemaClass;

import java.util.List;

/**
 * Molecule is an extension of ReferenceEntity to add additional information, facilitate the access to some already
 * existent attributes and implement compareTo.
 *
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class Molecule extends ReferenceEntity implements Comparable<Molecule>{
    private SchemaClass schemaClass;
    private String url;
    private boolean toHighlight; //whether the Molecule should be highlighted in the Molecules List
    private int occurrenceInPathway = 0;
    //private boolean disease; Changes for disease flag

    public Molecule(SchemaClass schemaClass) {
        super(schemaClass);
        this.schemaClass = schemaClass;
    }

    public void load(JSONObject jsonObject){
        super.load(jsonObject);

        toHighlight = false;
        //this.disease = disease;Changes for disease flag
        if(jsonObject.isObject().containsKey("url")){
            url = jsonObject.isObject().get("url").toString();
            url = url.substring(1, url.length()-1);
        }

        /*To use the images for Molecules consistently in the Molecules tab and in the Overview tab it is necessary
        * to switch from ReferenceEntity to PhysicalEntity.*/
        switch (schemaClass){
            case REFERENCE_GENE_PRODUCT:
            case REFERENCE_ISOFORM:
                this.schemaClass = SchemaClass.getSchemaClass(SchemaClass.ENTITY_WITH_ACCESSIONED_SEQUENCE.schemaClass);
                break;
            case REFERENCE_MOLECULE:
                this.schemaClass = SchemaClass.getSchemaClass(SchemaClass.SIMPLE_ENTITY.schemaClass);
                break;
        }
    }

    public String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    public boolean isToHighlight() {
        return toHighlight;
    }

    public void setToHighlight(boolean toHighlight) {
        this.toHighlight = toHighlight;
    }

    public int getOccurrenceInPathway() {
        return occurrenceInPathway;
    }

    public void setOccurrenceInPathway(int occurrence) {
        this.occurrenceInPathway = occurrence;
    }

    public SchemaClass getSchemaClass() {
        return schemaClass;
    }

    @Override
    public ImageResource getImageResource() {
        switch (this.schemaClass){
            case ENTITY_WITH_ACCESSIONED_SEQUENCE:
                return DatabaseObjectImages.INSTANCE.entityWithAccessionedSequence();
            case SIMPLE_ENTITY:
                return DatabaseObjectImages.INSTANCE.simpleEntity();
            case CHEMICAL_DRUG:
                return DatabaseObjectImages.INSTANCE.chemicalDrug();
            case GENOME_ENCODED_ENTITY:
                return DatabaseObjectImages.INSTANCE.genomeEncodeEntity();
            case OTHER_ENTITY:
                return DatabaseObjectImages.INSTANCE.otherEntity();
            case POLYMER:
                return DatabaseObjectImages.INSTANCE.polymer();
            case REFERENCE_DNA_SEQUENCE:
                return DatabaseObjectImages.INSTANCE.referenceDNASequence();
            case REFERENCE_RNA_SEQUENCE:
                return DatabaseObjectImages.INSTANCE.referenceRNASequence();
        }
        Console.warn("No resource found for " + this.schemaClass, this);
        return DatabaseObjectImages.INSTANCE.exclamation();
    }

    @Override
    public int compareTo(Molecule o) {
        List<String> names1 = this.getName();
        List<String> names2 = o.getName();
        int value;
        if(names1 != null && names2 != null && names1.size() > 0 && names2.size() > 0){
            value = names1.get(0).toUpperCase().compareTo(names2.get(0).toUpperCase());
        }else if(names1 != null && names1.size() > 0){
            value = names1.get(0).toUpperCase().compareTo(o.getDisplayName().toUpperCase());
        }else if(names2 != null && names2.size() > 0){
            value = this.getDisplayName().toUpperCase().compareTo(names2.get(0).toUpperCase());
        }else{
            value = this.getDisplayName().toUpperCase().compareTo(o.getDisplayName().toUpperCase());
        }
        return value;
    }

    public Molecule addData(DatabaseObject data) {
        ReferenceEntity referenceEntity = (ReferenceEntity) data;
        Molecule molecule = (Molecule) referenceEntity;
        molecule.setToHighlight(this.isToHighlight());
        molecule.setUrl(this.getUrl());
        return molecule;
    }
}
