/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import org.jdom.Element;

/**
 *
 * @author proprietaire
 */
public class Voix {
    private static int generateurNouveauNumero = 0;
    private final int numero;

    /**
     * crée un objet voix à partir d'un objet MusicXML
     * <voice> x <\voice>
     * @param child
     */
    public Voix(Element child) {
        this(Integer.parseInt(child.getValue()));
    }

    /**
     *
     * @return le numéro de la voix
     */
    public int getNumero() {
        return numero;
    }
    
    

    
    public Voix(int numero)
    {
        this.numero = numero;
        generateurNouveauNumero++;
    }
    
    
    public Voix()
    {
        this(generateurNouveauNumero);
    }

    public int compareTo(Voix voix) {
        if(getNumero() < voix.getNumero())
            return -1;
        else if(getNumero() == voix.getNumero())
            return 0;
        else
            return 1;
    }
    
    
    
    @Override
    public boolean equals(Object voix)
    {
        if(voix instanceof Voix)
        {
             return (getNumero() == ((Voix) voix).getNumero());
        }
        else
            return false;
    }

    @Override
    public int hashCode() {
        return getNumero();
    }






    
    /**
     *
     * @return un objet MusicXML qui contient le numéro de la voix
     * <voice> x <\voice>
     */
    public Element sauvegarder()
    {
        Element element = new Element("voice");
        element.addContent(String.valueOf( getNumero() ));
        return element;
    }
}
