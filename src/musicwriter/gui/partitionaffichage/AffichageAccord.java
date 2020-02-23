/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import musicwriter.Arithmetique;
import musicwriter.donnees.*;
import musicwriter.gui.*;

/**
 * Cette classe représente l'affichage d'un accord
 * (ensemble de notes jouées en même temps et appartenant à la même voix).
 * @author François Schwarzentruber
 */
public class AffichageAccord extends AffichageEnsemble {
    private double notePlusBasseY;
    private double notePlusHauteY;
    private double hampeyfin;
    private boolean hampeisaffiche = true;
    private final Accord accord;
    private final Systeme systeme;
    private boolean isContientDeuxNotesTresProches = false;
    private Note.HampeDirection hampeDirection;


    public Accord getAccord() {
        return accord;
    }


    





    public AffichageAccord(Systeme systeme, double x, Accord accord) {
        super(x);
        
        this.systeme = systeme;
        this.accord = accord;
        
        notesEnregistrer();

        hampeDirection = getAccord().getNotes().iterator().next().getHampeDirection();
        hampeDirectionDefaut();
        hampesCalculer();
        
    }



    public void hampesCalculer()
    {
        calculerHampePositions();
    }




    private void notesEnregistrer()
    {


        
        for(Note note : accord)
        {
            if(accord.isNoteHautDessusNoteMoinsUn(note))
            {
                add(new AffichageNote(systeme, note, (float) ( getX() + getNoteRayon() * 2)));
                isContientDeuxNotesTresProches = true;
            }
            else
                add(new AffichageNote(systeme, note, (float) getX()));

        }
    }
        

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        
        if(isHampeAffiche())
            afficherHampe(g);

        afficherArticulationsEtOrnements(g);
    }


    public Set<AffichageNote> getAffichageNotes()
    {
        Set<AffichageNote> S = new HashSet<AffichageNote>();

        for(Affichage a : this)
        {
            S.add((AffichageNote) a);
        }
        return S;
    }


    /**
     *
     * @return vrai ssi une hampe est affichée
     */
    private boolean isHampeAffiche()
    {
        return hampeisaffiche;
    }


    public int getHampe1SiDirectionHaut()
    {
        if(isHampeVersLeHaut())
            return 1;
        else
            return -1;
    }



    public Note getPremiereNote()
    {
        return accord.getNotes().iterator().next();
    }


    /**
     *
     * @return le rayon d'une note de l'accord
     */
    public double getNoteRayon()
    {
        return getAffichageNote(getPremiereNote()).getNoteRayon();
    }




    /**
     *
     * @return retourne l'abscisse de la hampe (le trait vertical qui relie toutes
     * les notes de l'accord)
     */
    public double getHampeX() {
        if(hampeDirection == Note.HampeDirection.BAS)
        {
               if(isContientDeuxNotesTresProches)
                     return getX() + getNoteRayon();
               else
                   return getX() - getNoteRayon()*1.15;
        }
        else
              return getX() + getNoteRayon();
    }


    /**
     * 
     * @return  le point où le trait de la hampe commence (côté notes)
     */
    public Point getHampeDebutPoint()
    {
        return new Point((int) getHampeX(), (int) getHampeYDebutCoteNotes());
    }
    
    
    /**
     *
     * @return le point où il y a l'extrémité de la hampe
     */
    public Point getHampeFinPoint()
    {
        return new Point((int) getHampeX(), (int) getHampeYFin());
        
    }


    /**
     *
     * @return l'ordonnée de la fin de la hampe
     */
    public double getHampeYFin() {
        return hampeyfin;
    }


    
    public void setHampeYFin(double hampeyfin)
    {
        this.hampeyfin = hampeyfin;
    }

    public int getNombreTraitsCroche() {
        if(isAppogiature())
        {
            return getDuree().getNombreTraitsCroche()-3;
        }
        else
            return getDuree().getNombreTraitsCroche();
    }


    public Moment getMomentDebut() {
        return getAccord().getDebutMoment();
    }

    public Moment getMomentFin() {
        return getAccord().getFinMoment();
    }

    public Voix getVoix() {
        return accord.getVoix();
    }


    
    private void afficherHampe(Graphics g) {
             g.drawLine(getHampeX(), 
                        getHampeHautY(),
                        getHampeX(), 
                        getHampeBasY());

    }


    public double getHampeHautY()
    {
        return  Math.min(hampeyfin, notePlusHauteY);
    }
    
    public double getHampeBasY()
    {
        return  Math.max(hampeyfin, notePlusBasseY);
    }
    
    /**
     *
     * @param note
     * @return l'afficheur de la note note et null si la note n'appartient pas à
     * l'accord
     */
    public AffichageNote getAffichageNote(Note note)
    {
        for(Affichage a : this)
        {
            if(((AffichageNote) a).getElementMusical() == note)
            {
                return ((AffichageNote) a);
            }
        }
        return null;
    }


    /**
     *
     * @return la durée des notes de l'accord
     */
    public Duree getDuree() {
        return getAccord().getDuree();
    }


    /**
     *
     * @return vrai ssi la hampe est vers le haut (en vrai, c'est à dire
     * si le paramètrage est automatique... ça donne quand même haut ou bas)
     */
    public boolean isHampeVersLeHaut() {
        return hampeDirection == Note.HampeDirection.HAUT;
    }


    /**
     * dit que la hampe est vers le haut... cette fonction est appelée pour
     * calculer l'affiche. Si la direction de la hampe n'est pas automatique et fixée
     * alors cette fonction ne modifie pas la direction de la hampe qui est écrite en dure
     * dans les notes
     */
    public void setHampeVersLeHautSiAutomatique()
    {
        if(isHampeDirectionAutomatique())
        {
            hampeDirection = Note.HampeDirection.HAUT;
            calculerHampePositions();
        }

    }


    
    public void setHampeVersLeBasSiAutomatique()
    {
        if(isHampeDirectionAutomatique())
        {
            hampeDirection = Note.HampeDirection.BAS;
            calculerHampePositions();
        }
    }
    

    
    public void setHampeDirectionSiAutomatique(Note.HampeDirection hampeDirection)
    {
        if(isHampeDirectionAutomatique())
        {
            this.hampeDirection = hampeDirection;
            calculerHampePositions();
        }
    }


    /**
     *
     * @return la direction de la hampe de la note.
     * En haut ou en bas !
     */
    public Note.HampeDirection getHampeDirection()
    {
        return this.hampeDirection;
    }

/**
 *
 * @return la portée de la première note de l'accord
 */
    public Portee getPortee()
    {
        return getPremiereNote().getPortee();
    }

    /**
     *
     * @return un rectangle qui contient le trait de la hampe.
     * Cette fonction est utilisée pour savoir si une autre note est sur la hampe
     * etc... et savoir si il faut modifier les directions des hampes
     */
    public Rectangle getHampeRectangle()
    {
//        double hampeyfinaccentue = hampeYFinCalculer(5 * systeme.getInterLigne(getPortee()));
//
//        return new Rectangle((int) (getHampeX() - 1.5*systeme.getInterLigne(getPortee())),
//                             (int) (Math.min(hampeyfinaccentue, hampeyNotePlusHaute)),
//                             (int) ( 3*systeme.getInterLigne(getPortee()) ),
//                             (int) (Math.max(hampeyfinaccentue, hampeyNotePlusBasse) - Math.min(hampeyfinaccentue, hampeyNotePlusHaute)));

        if(isHampeVersLeHaut())
        {
            return RegionFactory.createRectangle((int) (getHampeX() - 1.5*systeme.getInterLigne(getPortee())),
                            (int) hampeyfin,
                            (int) ( 3*systeme.getInterLigne(getPortee()) ),
                            (int) (notePlusBasseY - hampeyfin));

        }
        else
            return RegionFactory.createRectangle((int) (getHampeX() - 1.5*systeme.getInterLigne(getPortee())),
                            (int) notePlusHauteY,
                            (int) ( 3*systeme.getInterLigne(getPortee()) ),
                            (int) (hampeyfin - notePlusHauteY));
        
    }




    /**
     * si la direction est automatique, on met la hampe en haut ou en bas suivant la position
     * de la note sur la portée.
     * Si la note est plutôt en bas, la hampe sera vers le haut ; si la note est vers le haut (aiguë),
     * on mettra la hampe vers le bas.
     */
    private void hampeDirectionDefaut()
    {
        if(isHampeDirectionAutomatique())
        {
            Note notelaplusbasse = accord.noteLaPlusBasse();
            int notelaplusbasse_coordVerticale = notelaplusbasse.getCoordonneeVerticaleSurPortee();
            if(notelaplusbasse_coordVerticale > 2)
                hampeDirection = Note.HampeDirection.BAS;
            else
                hampeDirection = Note.HampeDirection.HAUT;
        }
    }


/**
 *
 * @param hampeLongueur
 * @return retourne le y final (le bout) de la hampe en fonction de la direction de la hampe
 */
    private double hampeYFinCalculer(double hampeLongueur)
    {
        if(isHampeVersLeHaut())
                return notePlusHauteY - hampeLongueur;
        else
                return notePlusBasseY + hampeLongueur;
    }

    
    private void calculerHampePositions() {
        
        Note notelaplusbasse = accord.noteLaPlusBasse();
        Note notelaplushaute = accord.noteLaPlusHaute();

        notePlusBasseY = systeme.getY(notelaplusbasse.getPortee(),
                                 notelaplusbasse.getCoordonneeVerticaleSurPortee());

        notePlusHauteY = systeme.getY(notelaplushaute.getPortee(),
                                 notelaplushaute.getCoordonneeVerticaleSurPortee());

        hampeyfin = hampeYFinCalculer(3 * systeme.getInterLigne(getPortee()));


        hampeisaffiche = (accord.getDuree().getRational().isStrictementInferieur(new Rational(4, 1)));


    }
    
    
    
    

    
    
    public double getMaxY()
    {
        return getRectangle().getMaxY();
    }

    
    public double getMinY()
    {
        return getRectangle().getMinY();
    }


    /**
     *
     * @param extremitePoint
     * @return true ssi le point extremitePoint passé en paramètre est proche
     * de la fin de la hampe
     */
    public boolean isProcheHampeFin(Point extremitePoint) {
        if(isHampeAffiche())
        {
            if(Math.abs(extremitePoint.getX() - getHampeX()) < 4)
            {
                if(getHampeDirection().equals(hampeDirection.HAUT))
                {
                    return Arithmetique.is1in23(extremitePoint.getY(),
                                   getHampeYFin()-3,
                                   getMinY());
                }
                else
                    return Arithmetique.is1in23(extremitePoint.getY(),
                                   getMaxY(),
                                   getHampeYFin()+3);
            }
            else
                return false;
        }
        else
            return false;

    }



    /**
     *
     * @return vrai ssi la note est une appogiature
     */
    private boolean isAppogiature() {
        return getPremiereNote().isAppogiature();
    }

    /**
     * 
     * @return vrai ssi la direction de la hampe est automatique
     */
    private boolean isHampeDirectionAutomatique() {
        return getPremiereNote().getHampeDirection().equals(Note.HampeDirection.AUTOMATIC);
    }

    private void afficherArticulationsEtOrnements(Graphics g) {
        double x = getX();
        double y = getHampeYDebutCoteNotes();

        ArrayList<String> lestrucs = new ArrayList<String>();
        lestrucs.addAll(getPremiereNote().getOrnements());
        lestrucs.addAll(getPremiereNote().getArticulations());

        double scale = getSysteme().getInterLigne(getPortee())*0.15/8.0f;


        

        for(String s : lestrucs)
        {
            Image img = ImageLoader.getImage(s + ".png");

            if(!isHampeVersLeHaut())
                y += getHampe1SiDirectionHaut() * img.getHeight()*scale;

            
            g.drawImage(img,
                        (x - img.getWidth()*scale/2.0f),
                        y,
                        (img.getWidth()*scale),
                        (img.getHeight()*scale));

            if(isHampeVersLeHaut())
                y += getHampe1SiDirectionHaut() * img.getHeight()*scale;
        }
        
        
    }


    /**
     *
     * @return le y de la fin de la hampe côté là où il y a les notes
     */
    private double getHampeYDebutCoteNotes() {
        if(isHampeVersLeHaut())
        {
            return notePlusBasseY;
        }
        else
        {
            return notePlusHauteY;
        }
    }

    public Systeme getSysteme() {
        return systeme;
    }


    /**
     * affiche la hampe comme si elle allait jusqu'au curseur curseur
     * (utilisé par l'interface utilisateur quand on veut rajouter
     * une note à un accord déjà existant)
     * @param g
     * @param curseur
     */
    public void afficherHampeJusqua(Graphics g, Curseur curseur) {
        if(isHampeAffiche())
            g.drawLine((int) getHampeX(),
                        (int) notePlusHauteY,
                        (int) getHampeX(),
                        (int) getSysteme().getY(curseur));
    }

    @Override
    public String toString() {
        return "display of " + getAccord().toString();
    }


    

}
