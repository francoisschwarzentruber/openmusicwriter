/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.controller;

import musicwriter.donnees.actions.PartitionActionSelectionInserer;
import musicwriter.gui.partitionaffichage.PartitionVue;
import musicwriter.donnees.*;
import java.awt.Color;
import musicwriter.gui.Graphics;
import musicwriter.donnees.actions.PartitionActionSelectionRemplacer;


/**
 * Cette classe gère le déplacement à la souris d'une sélection d'éléments musicaux.
 * Ca gère la transposition (déplacement vertical), changement de portées (déplacement vertical),
 * changement de moments (déplacement horizontal).
 * @author Ancmin
 */
public class NotesDeplacementSouris
    {
        private final Curseur referenceCurseur;
        private final Selection selection;
        private final Controller partitionPanel;
        private Curseur referenceCurseurFutur;
        private boolean effectuerCopie = false;

        private static final Color colorElementMusicalDeplace = PartitionPanelModeSelection.getColorSelection();


        public NotesDeplacementSouris(Controller partitionPanel, Curseur referenceCurseur, Selection selection)
        {
            this.referenceCurseur = referenceCurseur;
            this.referenceCurseurFutur = referenceCurseur.clone();
            this.selection = selection;
            this.partitionPanel = partitionPanel;
        }




        private PartitionDonnees getPartitionDonnees()
        {
            return (PartitionDonnees) getPartitionVue().getPartitionDonnees();
        }

        public void setReferenceCurseurFutur(Curseur referenceCurseurFutur)
        {
            this.referenceCurseurFutur = referenceCurseurFutur;
        }



        public Curseur getReferenceCurseur() {
            return referenceCurseur;
        }

        public Curseur getReferenceCurseurFutur() {
            return referenceCurseurFutur;
        }


    






        /**
         * effectue le déplacement
         */
        public void effectuer() {
            if(!isTrivial())
            {
                Selection selectionFutur = getSelectionFutur();
                getHistoire().executer(
                        new PartitionActionSelectionRemplacer(selection, selectionFutur ));
                partitionPanel.calculer(Moment.min(getSelection().getMomentDebut(),
                                                   selectionFutur.getMomentDebut()));
                partitionPanel.setSelection(selectionFutur);
                
            }
            else
            {
                partitionPanel.setSelection(getSelection());
            }
        }


        /**
         * effectue une copie
         */
        public void effectuerCopie() {
            if(!isTrivial())
            {
                Selection selectionFutur = getSelectionFutur();
                getHistoire().executer(
                        new PartitionActionSelectionInserer( selectionFutur ));
                partitionPanel.calculer(Moment.min(getSelection().getMomentDebut(),
                                                   selectionFutur.getMomentDebut()),
                                        Moment.max(getSelection().getFinMomentElementMusicalDebute(),
                                                   selectionFutur.getFinMomentElementMusicalDebute()));
                partitionPanel.setSelection(selectionFutur);
                
            }
            else
            {
                partitionPanel.setSelection(getSelection());
            }
        }


        


        /**
         * affiche les éléments musicaux flottants qu'on est en train de déplacer
         * @param g
         */
        public void afficher(Graphics g)
        {
            g.setColor(colorElementMusicalDeplace);
            g.setPenWidth(2);
            getPartitionVue().afficherSelectionDehors(g, getSelectionFutur());
        }










/**
 *
 * @return la sélection mais déplacée sur la portée, moment indiqué par la souris
 * + transposée aussi.
 */
        private Selection getSelectionFutur()
        {
            final Selection selectionfutur = new Selection();

            final Intervalle intervalle = getIntervalle();
            final Duree dureeDeplacement = getDureeDeplacement();


            for(ElementMusical el : getSelection().getElementsMusicaux())
            {
                final ElementMusical elnouveau = (ElementMusical) el.clone();
                elnouveau.deplacerRelatif(dureeDeplacement, intervalle, getPartitionDonnees(), getPorteeDeplacement());
                selectionfutur.elementMusicalAjouter(elnouveau);




            }

            return selectionfutur;


        }






/**
 *
 * @return true ssi le déplacement est trivial... autrement dit, true ssi rien n'est
 * fait : pas de transposition, pas de changement de portées et pas de déplacement horizontal
 */
        public boolean isTrivial() {
            return getIntervalle().isNull() &&
               getDureeDeplacement().isZero() &&
               (getPorteeDeplacement() == 0);
        }

        private PartitionVue getPartitionVue() {
            return partitionPanel.getPartitionVue();
        }



        private Selection getSelection() {
            return selection;
        }


        private Intervalle getIntervalle()
        {
            return new Intervalle(this.referenceCurseur.getHauteur(),
                                  this.referenceCurseurFutur.getHauteur());
        }


        private Duree getDureeDeplacement()
        {
            return new Duree(this.referenceCurseur.getMoment(),
                             this.referenceCurseurFutur.getMoment());
        }



        private int getPorteeDeplacement()
        {
            if(this.referenceCurseur.getPortee() != null)
                   return this.referenceCurseurFutur.getPortee().getNumeroAffichage()
                        - this.referenceCurseur.getPortee().getNumeroAffichage();
            else
                return 0;
        }



        private Histoire getHistoire()
        {
            return partitionPanel.getHistoire();

        }

        public void setModeEffectuerCopie() {
            effectuerCopie = true;
         //   partitionPanel.modifierSourisCurseurMainQuiPrendPlus();
        }

        public void setModeEffectuerDeplacement() {
            effectuerCopie = false;
         //   partitionPanel.modifierSourisCurseurMainQuiPrend();
        }




        public boolean isEffectueraCopie()
        {
            return effectuerCopie;
        }

        public void setAlterationMoins() {
            final int a = referenceCurseurFutur.getHauteur().getAlteration().getNumero();
            referenceCurseurFutur.getHauteur().setAlteration(a-1);
        }


        public void setAlterationPlus() {
            final int a = referenceCurseurFutur.getHauteur().getAlteration().getNumero();
            referenceCurseurFutur.getHauteur().setAlteration(a+1);
        }
        
    }
