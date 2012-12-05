/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.datatypes.scheduling;

/**
 *
 * @author uqlpayne
 */
public class TimePeriod extends TimeBlock {

    protected int quantum;

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }
}
