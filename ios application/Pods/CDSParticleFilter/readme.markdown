# CDSParticleFilter
**CDSParticleFilter** implements a simple particle filter in Objective C, for use on iOS or OSX platforms.  It includes a simple particle filter, as well as **CDSXYParticleFilter**, a 2-dimensional state (X-Y) particle filter with more defaults set up that's simpler to use.

CDSParticleFilter is still in beta.  Please submit any bugs or feature requests at [https://bitbucket.org/codeswell/cdsparticlefilter/issues](https://bitbucket.org/codeswell/cdsparticlefilter/issues).


## Quick Instructions
### Add the files to your project
You can download the files and add the contents of the `Source` directory directly or use [CocoaPods](http://cocoapods.org) by adding this line to your podfile:

`pod 'CDSParticleFilter', :git => 'git@bitbucket.org:codeswell/cdsparticlefilter.git'`

### Instantiate a particle filter
For example, to instantiate the 2-d particle filter, this line will create this filter with 250 particles and a state space of -100 <= x <= 100 and -100 <= y <= 100.  The initial particles will be randomly/uniformly distributed across the state space and the filter will apply gaussian noise with σ = noiseSigma each time the particles are resampled.

`
CDSXYParticleFilter* myparticleFilter = [[CDSXYParticleFilter alloc] initWithParticleCount:250 minX:-100.0 maxX:100.0 minY:-100.0 maxY:100 noiseSigma:0.10];
`
### Implement the particle filter delegate
The delegate methods will be called to allow you to apply motion to each particle and weight each particle for resampling selection.

### Call the methods to iterate the filter
In your code, make calls to `applyMotion` and `applyMeasurements:` in turn.  After each call to `applyMeasurements:`, the filter will normalize the particle weights and resample/regenerate particles.

# More information, links
## Project home page
[https://bitbucket.org/codeswell/cdsparticlefilter](https://bitbucket.org/codeswell/cdsparticlefilter)

## Class reference documentation
[http://cocoadocs.org/docsets/CDSParticleFilter](http://cocoadocs.org/docsets/CDSParticleFilter)

## Particle filters
[A quick video showing particle filters in action](http://www.tubechop.com/watch/1753927)  ([Here's the larger video from which this is extracted](http://www.youtube.com/watch?v=H0G1yslM5rc))

[Wikipedia page on particle filters](http://en.wikipedia.org/wiki/Particle_filter)

## Codeswell (the author's) home page
[http://codeswell.com](http://codeswell.com)


