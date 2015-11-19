package com.inerre.taskmanager;

/**
 * Created by root on 11/11/15.
 * code & class from : https://github.com/pratikbutani/MultiSelectSpinner.git
 */
class KeyPairBoolData {
    int id;
    String name;
    String idString;
    boolean isSelected;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getIdString(){
        return idString;
    }

    public void setIdString(String idString){
        this.idString = idString;
    }
    /**
     * @return the isSelected
     */
    public boolean isSelected() {
        return isSelected;
    }
    /**
     * @param isSelected the isSelected to set
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
