package HPSOGO;

import java.util.ArrayList;
import java.util.List;

public class BestParticles {
    private List<Particle> personalBestParticles =null;
    private Particle globalBestParticle =null;
    public BestParticles(){
        personalBestParticles =new ArrayList<Particle>();
    }

    public List<Particle> getPersonalBestParticles() {
        return personalBestParticles;
    }

    public void setPersonalBestParticles(List<Particle> personalBestParticles) {
        this.personalBestParticles = personalBestParticles;
    }

    public Particle getGlobalBestParticle() {
        return globalBestParticle;
    }

    public void setGlobalBestParticle(Particle globalBestParticle) {
        this.globalBestParticle = globalBestParticle;
    }

}
