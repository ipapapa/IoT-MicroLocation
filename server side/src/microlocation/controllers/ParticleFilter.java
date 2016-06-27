package microlocation.controllers;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParticleFilter {

    Particle[] particles; // particles declared
    int numParticles = 0;  // number of particles initialized
    Random gen = new Random(); // random number generator

    public ParticleFilter(int numParticles, Point[] landmarks, double width, double height,double length) // constructor
    {
        this.numParticles = numParticles;

        particles = new Particle[numParticles];
        for (int i = 0; i < numParticles; i++) {
            particles[i] = new Particle(landmarks, width, height,length); //create new particles 
        }
    }

    public void setNoise(float Fnoise, float Tnoise, float Snoise) {
        for (int i = 0; i < numParticles; i++) {
            particles[i].setNoise(Fnoise, Tnoise, Snoise);
        }
    }

    public void move(double turn, double forward) throws Exception {
        for (int i = 0; i < numParticles; i++) {
            particles[i].move(turn, forward);
        }
    }

    public void resample(double[] measurement) throws Exception
    {
        Particle[] new_particles = new Particle[numParticles];

        for (int i = 0; i < numParticles; i++) {
            particles[i].measurementProb(measurement);
        }
        float B = 0f;
        Particle best = getBestParticle();
        int index = (int) gen.nextFloat() * numParticles;
        for (int i = 0; i < numParticles; i++) {
            B += gen.nextFloat() * 2f * best.probability;
            while (B > particles[index].probability) {
                B -= particles[index].probability;
                index = circle(index + 1, numParticles);
   
            }
            new_particles[i] = new Particle(particles[index].landmarks, particles[index].worldWidth, particles[index].worldHeight,particles[index].worldLength);
            new_particles[i].set(particles[index].x, particles[index].y,particles[index].z, particles[index].orientation, particles[index].probability);
            new_particles[i].setNoise(particles[index].forwardNoise, particles[index].turnNoise, particles[index].senseNoise);
     
        }

        particles = new_particles;        
    }

    private int circle(int num, int length) {
        while (num > length - 1) {
            num -= length;
        }
        while (num < 0) {
            num += length;
        }
        return num;
    }
    
    public Particle getBestParticle() {
        Particle particle = particles[0];
        for (int i = 0; i < numParticles; i++) {
            if (particles[i].probability > particle.probability) {
                particle = particles[i];
            }
           
        }
        return particle;
    }
    
    public Particle getAverageParticle() {
        Particle p = new Particle(particles[0].landmarks, particles[0].worldWidth, particles[0].worldHeight,particles[0].worldLength);
        float x = 0, y = 0,  z = 0,orient = 0, prob = 0;
        for(int i=0;i<numParticles;i++) {
            x += particles[i].x;
            y += particles[i].y;
            z += particles[i].z;
            orient += particles[i].orientation;
            prob += particles[i].probability;
        }
        x /= numParticles;
        y /= numParticles;
        z/=numParticles;
        orient /= numParticles;
        prob /= numParticles;
        try {
            p.set(x, y, z, orient, prob);
        } catch (Exception ex) {
            Logger.getLogger(ParticleFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        p.setNoise(particles[0].forwardNoise, particles[0].turnNoise, particles[0].senseNoise);
        
        return p;
    }

    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < numParticles; i++) {
            res += particles[i].toString() + "\n";
        }
        return res;
    }
   
}