package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.species;

import org.reactome.web.analysis.client.model.SpeciesSummary;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class Species {
    private Long id;
    private String name;
    private SpeciesSummary speciesSummary;
    private boolean isChecked = true;

    public Species(final SpeciesSummary speciesSummary) {
        this.id = speciesSummary.getTaxId();
        this.name = speciesSummary.getName();
        this.speciesSummary = speciesSummary;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SpeciesSummary getSpeciesSummary() {
        return speciesSummary;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "Species{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
