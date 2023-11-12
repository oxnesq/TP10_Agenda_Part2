package agenda;

import java.time.LocalDate;
import java.util.*;

/**
 * Description : An agenda that stores events
 */
public class Agenda {
    private ArrayList<Event> theEvents = new ArrayList<Event>();

    public Agenda() {

    }

    /**
     * Adds an event to this agenda
     *
     * @param e the event to add
     */

    public void addEvent(Event e) {
        theEvents.add(e);
    }


    public List<Event> eventsInDay(LocalDate day) {
        ArrayList<Event> eventThisDay = new ArrayList<Event>();
        for (Event e: theEvents){
            if (e.isInDay(day)) eventThisDay.add(e);
        }
        return eventThisDay;
    }

    /**
     * Trouver les événements de l'agenda en fonction de leur titre
     * @param title le titre à rechercher
     * @return les événements qui ont le même titre
     */
    public List<Event> findByTitle(String title) {
        ArrayList<Event> byTitle = new ArrayList<Event>();
        for (Event e : theEvents){
            if (e.getTitle().equals(title)){
                byTitle.add(e);
            }
        }
        return byTitle;
    }

    /**
     * Déterminer s’il y a de la place dans l'agenda pour un événement
     * @param e L'événement à tester (on se limitera aux événements simples)
     * @return vrai s’il y a de la place dans l'agenda pour cet événement
     */
    public boolean isFreeFor(Event e) {
        Boolean bo =true;
        for (Event alreadyIr : theEvents){
            if (alreadyIr.getStart().isBefore(e.getStart())){
                // simple commence avant e mais finit apres debut de e
                if (alreadyIr.getStart().plus(alreadyIr.getDuration()).isAfter(e.getStart()) ){
                    bo=false;
                }
            } else if (alreadyIr.getStart().equals(e.getStart())){
                // simple et e commencent en mm temps
                bo=false;
            }if (alreadyIr.getStart().isAfter(e.getStart())){
                // simple commence apres e mais avant sa fin
                if (e.getStart().plus(e.getDuration()).isAfter(alreadyIr.getStart()) ){
                    bo=false;
                }}
        }
        return bo;
    }
}
