Jscience is a general scientific library in Java.
- Actually, your basic survival kit as a developer looking for science -



This is the readme.txt file for Episteme (http://jade.dev.java.net/) version 1.0 beta 1 EXPERIMENTAL, as of 20th November 2007.


We have currently released (in the "documents and files" section of http://jade.dev.java.net) all four planned distributions:
- level 1 release for environments short on memory (PDA...) which contains awt, help, io, mathematics, measure.random, media, ml (xml parsers), net, swing, text and util packages.
- level 2 release which contains the level 1 and architecture, astronomy, bibliography, chemistry, computing, devices, earth (but not weather subpackage), engineering and physics.
- level 3 release which contains the level 2 and arts, biology, earth.weather, economics, geography, history, law, linguistics, measure (and subpackages but random which is already in level 1), medicine, philosophy, politics, psychology, sociology and sports.

(There is also a distribution 
containing only the data files for people not interested in the Java library.)


WARNINGS:
1.
- Currently missing from release level 2: some astronomy and chemistry files (about 60 files)
- Currently missing from release level 3: some economics and history files (about 5 files)

2.
This code is currently unrelated to Episteme OFFICIAL that is distributed at www.episteme.org: we expect to merge some day but we currently offer a separate developement trunk with many more features but much less support.

3.
- All packages and data are delivered under GPL license EXCEPT a couple packages using specific licenses:
	org.episteme.astronomy.solarsystem.artificialsatellites
	org.episteme.awt.icons
	org.episteme.biology.taxonomy
	org.episteme.geography
	org.episteme.computing.automaton
	org.episteme.linguistics.search
	org.episteme.mathematics.geometry
	org.episteme.measure.random
        org.episteme.media.audio (data files)
        org.episteme.media.picture (data files)
        org.episteme.medicine (data files)
	org.episteme.ml.gml (and the corresponding schema)
	org.episteme.ml.om (and the corresponding schema)
	org.episteme.ml.sbml (and the corresponding schema)
	org.episteme.physics.nuclear.kinematics
	org.episteme.sociology.forms
- Many of these licenses are quite similar to GPL which are usually called "compatible". See license.txt in this directory and license.txt in corresponding directories for appropriate usage.


In the org.episteme directory (this directory) you will find:
blank_page.html a blank page.
craftinghierarchy.dtd and craftinghierarchy.xml provide some insight concerning the way we design new tools upon previous discoveries.
dukelogo.tif and dukelogo.gif are the Episteme logo, based on the Java Duke logo from Sun Microsystems, see section 6, contributions.
FallOfMan-Cranach.jpg (a painting by Cranach the Elder circa 1540) should remind you that for many religions (especially christians) the tree of knowledge (which is basically what Episteme is) is a forbidden fruit.
index.html the html version of this file.
javacodingrecommendations.txt some coding additional recommendations for developers.
javaCodingStandards.pdf a complete coding recommendation manual for developers.
illustrationssymbols.pdf (adobe acrobat document) is a very useful set of graphics for a poster presentation.
javascientificpackages.html and javascientificpackages.zip contain bookmarks to every existing Java scientific library on the market.
epistemedistributions.pdf (adobe acrobat document) and epistemedistributions.odg (openoffice document) give an overview of the architecture of packages in Episteme.
epistemeicon.png a smaller version of dukelogo.tif.
levels.txt some eplanation about Episteme distributions (see also epistemedistributions.pdf) or top of this document.
license.txt the GPL 2.0 license.
MANIFEST.MF the java archive manifest file.
openproblems.html A list of scientific unresolved questions.
packagesicons.zip (zip compressed document) is a set of icons for each corresponding science package.
safetysigns.pdf (adobe acrobat document) as used in laboratories.
scaleofsciences.pdf (adobe acrobat document) and scaleofsciences.odg (openoffice document) are mostly here for illustration to show how sciences are distributed of the size of things (also see http://www.documaga.com/wp-content/uploads/2010/02/scaleofuniverse.swf).
ScienceProjectsWebpages.zip some possible research projects for every science.
sciences.xml and scientifichierarchy.xml give a definition of science names.
scienceresults.txt describes the main achieved scientific results in every field up to today.
scientificmethod.txt describes the principles of the scientific methodology.
sourceforgescientificpackages.html and sourceforgescientificpackages.zip contain bookmarks to sourceforge only Java scientific oriented projects.
todo.txt gives a list of limitations and features we may work on.
wsdl.html lists useful WSDL services to grab data from.


Please read the EXTRA WARNINGS section (section 5 of this file).

------------------------------------------------------------------------------------------

1. FEATURES
2. INSTALLATION FOR END USERS
3. COMPILATION AND JAVADOC FOR DEVELOPERS
4. REQUIRED LIBRARIES
5. EXTRA WARNINGS
6. CONTRIBUTIONS
7. USED PACKAGES
8. EPISTEME MARKET SHARE
9. RELEASE HISTORY
10. SELECTED READINGS OR SOURCES
11. JAVA SPECIFIC LINKS
12. SCIENCE PACKAGE SPECIFIC LINKS
13. CONTACT US

------------------------------------------------------------------------------------------


1. FEATURES:

This is a general scientific library in Java. Is is as complete in each area as it can be although we have made every effort to keep it simple and easy to use. This is therefore a kind of survival kit or a portable lab.

Our coding standards are namely:
- Object Oriented: Objects/Interfaces are first class citizens (no utility methods)
- Generic (no primitive types, interfaces instead)
- Simple (even for the profane)
- Useful Classes exist for a purpose
- Easy to use
- Standards oriented
- XML oriented
- Documented

Why using Episteme and not another package ? Well, Episteme is a collective effort which has the aim to be maintained and enhanced over the time. Episteme is also quite useful because you can submit your subpackages for addition into the framework for the benefits of everyone. Finally we make a lot of effort to always provide the fastest algorithms in a strong architecture, something that is quite difficult to achieve, especially for specialized packages.

This is an API, not an application per se. Mostly, it is an API to develop some others API or some applications. We therefore expect a different target audience than the mainstream internet user. In fact we target well defined sub groups:
- API builders to design APIs on top of this one
- Corporate developers looking for a wide spectrum free API
- Applet/Webstart developers that need to quick develop a product
- Researchers and graduate students that want to test something and (may be) don't require to build a full application

Episteme, and especially the numerical intensive packages (mostly mathematics), uses two models. The first one is the traditional way in which scientific libraries are designed, is optimized for speed and relies heavily on ints and doubles, while offering a strong logic. The other one provides an even better model closer to theoritical concepts generally acknowledged in science. It is based on ExactIntegers and ExactReals which both provide infinite precision support at the sacrifice of some speed. It comes along with unit systems and quantities. It is much less developed in the current release than the traditional model mostly for the reason that it makes this API bigger and more complex while probably required by less users. There are some bridges that help switch from one model from another so you shouldn't have much difficulites using any of the two. Classes normally behave the same way in both models and mostly only differ by their names.

If you want to have an overview about how the packages are organized (object oriented and encapsulation), look at the org.episteme.sociology.Person class. Person extends Human which itself extends HistoricalIndividual which itself extends Individual, itself basically a Set Of Organs, itself a Set of Tissues, itself a Set of Cells which contain DNA or RNA. These are big Molecules made up of Atoms moving as physical Particles. Many other classes are used in the same way.


The library supports almost everything you should expect (except Education, see section 11 for solutions). It covers the following fields:
Architecture
Arts
Astronomy
Bibliography
Biology
Chemistry
Computing
Earth Sciences
Economics
Engineering
Geography
History
Law
Linguistics
Mathematics (which accounts for 20 percent of the classes, no more no less)
Measure (a general purpose measure scheme)
Medicine
Philosophy
Physics
Politics
Psychology
Sociology
Sports

And several devices, media filters, utils and widgets (beans).

There are also some XML parsers, namely MathML, OpenMath, CML, ObervationManager, SBML, gedcom, GML, TigerXML.

Some limitations are to be mentionned, see todo.txt for a complete explanation.

Finally there are many data files that are cobundled along with the classes. They are support information, NOT necessarily used by classes but brought to you so that you can have a handy reliable source of data.

Although we have made many efforts to produce such a library there is still room for a lot of improvements. Please report any inaccuray, bug, feature request through the Episteme website http://www.episteme.org/.


2. INSTALLATION FOR END USERS:

Apart from Java itself, the distribution contains all the required files to be ready to run.

Install Java (1.5 or later) from http://www.java.com/. All class files are Java VM 1.5 compatible (as far as we know) although most of the packages should compile with Java 1.3 if needed (notable exception: org.episteme.measure). See also the "Required libraries" section below.

a. Unzip the current version of episteme.zip.

b. either:
Put the Episteme library in the <jre>/lib/ext directory of your Java installation, removing all previous Jscience files.
Also include all the files in episteme/lib/ in the <jre>/lib/ext directory of your Java installation.
Note that you may write over existing files (which are probably older versions of the same libraries).
Or:
Remove any previous version of Episteme by simply deleting the org.episteme directory.
Make sure your CLASSPATH environment variable contains a path to episteme/classes/episteme.jar.
Make sure your CLASSPATH environment variable contains a path to episteme/lib/ (the packages needed by Episteme to run).

Run any sample with the command line, as usual.


3. COMPILATION AND JAVADOC FOR DEVELOPERS:

To compile all the code:
Install Java (1.5 or later) from http://www.java.com/. All class files are Java VM 1.5 compatible although most of the packages should compile with Java 1.3 if needed (notable exception: org.episteme.measure). The text encoding for the compiler must be UTF-8 (mainly for some Measure package classes) except for the org.episteme.computing.game package (which should be set as default encoding) and org.episteme.mathematics.geometry (cp1252). See also the "Required libraries" section below. Compile with the command line, as usual or with Ant (http://ant.apache.org/) using the supplied build.xml in the org package.

Javadoc shouldn't be generated for some classes in org.episteme.geography.coordinates, org.episteme.mathematics.algebraic.numbers, org.episteme.mathematics.geometry and org.episteme.physics.solids see the readme.txt in the corresponding package, as these classes are not part of the API.


4. REQUIRED LIBRARIES:

To compile and/or run all the classes in this library you will also need several libraries (list below). These libraries have been included in the lib directory so that you just need to drag and drop them to you Java /lib/ext folder.

J2EE (j2ee.jar) for the org.episteme.sociology.forms package. J2EE can be found at http://java.sun.com/javaee/index.jsp
Java3D for the org.episteme.astronomy package (among others). Java3D can be found at http://java.sun.com/products/java-media/3D/index.jsp.
JavaComm for the control of the LX200 or NexStar telescope (org.episteme.devices package). JavaComm can be found at http://java.sun.com/products/javacomm/index.jsp.
JavaMail for the notification mailer (org.episteme.net.progress). JavaMail can be found at http://java.sun.com/products/javamail/index.jsp (it is also included in the J2EE jar file). JavaMail is itself dependent upon JAF (Java Activation Framework) that can be found at http://java.sun.com/products/javabeans/jaf/.
OpenBQS client (BQS_ws.jar) for org.episteme.bibliography package found at http://www.ebi.ac.uk/~senger/openbqs/
JGraph for the org.episteme.computing.graph package at http://www.jgraph.com/
AntLR for the org.episteme.computing.ai.planning package at http://www.antlr.org/
Javolution for the org.episteme.measure package at http://www.javolution.org/
The XML parsers (org.episteme.ml subpackages) require several XML libraries:
Xerces XML parser found at http://xml.apache.org/xerces2-j/
JDom XML parser found at http://www.jdom.org/
JAXEN XML parser found at http://www.jaxen.org/

Remember that you only need these libraries if you use the corresponding package or plan to recompile everything.

Optional libraries:
tools.jar (JDK doclet system) from the JDK lib directory must be in the classpath to compile the org.episteme.help.doclet subpackage. Warning, only version 1.5 of the tool.jar library (distributed with Java JDK 1.5) is supported, it won't compile with previous versions. This package is mostly needed if you want to recompile Episteme and of no use as a developer.
JUnit for org.episteme.test subpackage found at http://www.junit.org/index.htm. This package is needed only if you want to test Episteme and of no use as a developer.
Ant build tool to automate compilation found at http://ant.apache.org/

All these are also bundled in the lib directory along with the source code of Episteme.

Note about the MathML 2.0, 2nd edition implementation and dtd:
The MathML (partial) implementation that can be found on the Jscience website is different from the standard implementation of the MathML dtd (http://dev.w3.org/cvsweb/java/classes/org/w3c/dom/mathml/) and corrects minor bugs. Please download the version from episteme named MathML DOM. You will find the MathML dtd at http://www.w3.org/TR/2003/REC-MathML2-20031021/

5. EXTRA WARNINGS:

Though we made every possible effort to produce something that really rocks, we make no warranty of any sort about the results produced by using this library or whatsoever. You may actually loose zillions, crash your probe on Mars using the library which is provided as such and that you use at your own risk.

This is a scientific package. Futures release may NOT be compatible with the current code from the library. This means that code may NOT be upward compatible. One reason is that science changes from time to time and perhaps contradicts itself, wich means that we may have to rewrite the code because "it is not scientific anymore". However, we will make every effort so that there is very few effort on your side to update your code to a new version. Our philosophy is to produce a new version to provide more features, more precision, more efficiency, less bugs.

The library comes with several viewers that are currently designed by third parties and are merely cobundled and not fully integrated. You should consider these viewers as deprecated, although they are not marked as such.
This includes: org.episteme.astronomy.ephemeris, org.episteme.astronomy.milkyway, org.episteme.astronomy.solarsystem.constellations (as well as the factories for Comets, Asteroids and Artificial Satellites), org.episteme.chemistry.viewer (and subpackages), org.episteme.earth.weather.cityforecast and org.episteme.earth.weather.metar.

This is quite a large library. It is therefore full of errors, inconsistencies and innacuracies. Yelling is not the solution. Please, fill a request for enhancement, a bug report or drop us a mail for every missing contribution, copyright infringment or the like. We are working hard to make this library as good as possible and will make every effort to provide a rapid answer.


6. CONTRIBUTIONS:

We have asked and received permission to use all the code and files we did not produce ourselves.

Safety signs pdf file taken from http://www.urwpp.de/common/pdf/cd_l_safetysigns.pdf or http://www.mr-clipart.com/products/safety/safetysigns-e.html. Cards in org.episteme.swing.cards taken from Oxymoron cards (http://www.waste.org/~oxymoron/cards/). Icons in org.episteme.awt.icons are from Sun Java Icons (http://java.sun.com/developer/techDocs/hi/repository/). Symbols pdf file taken from http://ian.umces.edu/conceptualdiagrams.php, "Symbols for diagrams courtesy of the Integration and Application Network (ian.umces.edu/symbols), University of Maryland Center for Environmental Science".

We also bundle many datafiles and physical measurements produced by many sources. Some of these files were converted to xml or reorganized by ourselves. You will normally find an accompying document (Java, html or text file) describing the original location(s).

The Duke logo, has its license unknown, based on a logo from Sun Microsystem: "the Duke logo and the Java Coffee Cup logo are trademarks or registered trademarks of Sun Microsystems, Inc. in the U.S. and other countries", see http://www.sun.com/policies/trademarks

Several people have contributed to this work. This list only includes active coders and known contributors of packages. Many people have also contributed and provided us with feature request, bug reports, etc. Big thanks to everyone. Send us a mail if you feel you have been forgotten (and you probably were).

To name a few:
Jean-Marie Dautelle <jean-marie@dautelle.com> (JADE, Javolution, units packages)
Greg Bush <GBush@HealthMarket.com> (mathematical prover)
Miguel Garvie <miguelgarvie@yahoo.co.uk> (distributed computing)
Levent Bayindir <levent@ceng.metu.edu.tr> (artificial intelligence)
Mark Hale <m.hale@qmul.ac.uk> (mathematics, physics, chemistry)
Kyle Siegrist <siegrist@math.uah.edu> (mathematics, probability)
Sergio A. de Carvalho Jr. <sergioanibaljr@yahoo.com.br> (biology alignment)
Edward A. Pier <eap@milkyway.gsfc.nasa.gov> (astronomy FITS)
Michel Van den Bergh <vdbergh@luc.ac.be> (network time)
Jim Baer <jimbaer1@earthlink.net> (astronomy)
Martin Senger <senger@ebi.ac.uk> (bibliography)
Carsten Knudsen <Carsten.Knudsen@fysik.dtu.dk> (mathematics)
Peter Csapo <peter@pcsapo.com> (astronomy)
David Caley <dcaley@germane-software.com> (astronomy)
Luke Reeves <luke@neuro-tech.net> (economics)
Michael H. Kay <mhkay@iclway.co.uk> (GedML)
Serge Knystautas <sergek@users.sourceforge.net> (earth sciences)
Doug Gehringer <Doug.Gehringer@sun.com> (medicine)
Paul Houle <paul@honeylocust.com> (random numbers)
Silvere Martin-Michiellot <silvere@digitalbiosphere.org> (soft sciences and biology, chemistry)
Richard Cannings (Eigenvalue methods)
Don Cross (Fourier transforms)
Jaco van Kooten (Statistics and some special functions)
Daniel Lemire (Wavelet package)
Taber Smith (Matrix trace and norms)
John Carr (awt/swing code and doc improvements)
Rodrigo Almeida Gon?ves (LPSolve port)
Benedikt Dietrich (Polynomials package)
Alasdair King <alasdairking@yahoo.co.uk> (braille)
"This product includes software developed by Galdos Systems Inc. (http://www.galdosinc.com)." (GML4J)
Adam Pease <apease@users.sourceforge.net> (linguistics)
Mark E. Shoulson <mark@kli.org> (calendars)
Joe Huwaldt <jhuwaldt@mac.com> (atmospheric systems)
Pablo Pita Leira <pablo.pita@pitagoral.com> (ADFC fluids system translation)
Paul Anderson <paul.d.anderson@comcast.net> (mathematics debugging)
Alex Lam <alexlamsl@gmail.com> (mathematics)

This is a opensource project, people just come in and join if they want to waste their time and feel like it.
We don't even know each others but come excited by the common goals.

You can offer a contribution in many ways:
- you have an idea and roadmap to rewrite one of the package in a better way
- you have an idea to improve a package without breaking the underlying model, offering new classes and methods
- you have designed a new subpackage you want to share
- you have improved existing documentation
- you have written some tests or sample code
- you want to submit a bug report
- you want to submit a request for enhancement
- you have some datafiles you would like us to bundle in
- you want to review or write some comments about Episteme or one of the pacakges
- you have written an application or an applet that uses Episteme and would like to let us know so that we can link to you

Don't hesitate to contact us should you want to contribute in a way or another.


7. USED PACKAGES:

This software is based on the work from several authors which code we used to produce our own code. Normally, we merge every feature from every package (a notable exception is Biojava).

We downloaded the code for (almost) each package on the 1st January 2005, so any later release should be more recent than the code we built Episteme upon.

Specifically:
Janet 1.10 beta (partial inclusion) (Carsten Knudsen <Carsten.Knudsen@fysik.dtu.dk>)
JSci 0.93 (Mark Hale <m.hale@qmul.ac.uk>) (still being developed)
OpenBQS 1.0 (Martin Senger <senger@ebi.ac.uk>)
Hipparcos 1.0 (koflaher@rssd.esa.int)
CODES (Comet/asteroid Orbit Determination and Ephemeris Software) 06/04 (Jim Baer <jimbaer1@earthlink.net>) (partial inclusion) (still being developed)
Tripos 1.0 (http://java.sun.com/products/java-media/3D/demos/index.html)
Biojava 1.4pre1 (biojava-dev@biojava.org) (very partial inclusion) (still being developed)
Neobio pre-alpha (Sergio A. de Carvalho Jr. <sergioanibaljr@yahoo.com.br>)
Peter Csapo 1.0 (Peter Csapo <peter@pcsapo.com>)
David Caley 1.0 (David Caley <dcaley@germane-software.com>)
JRUG JRL 1.0 (Levent Bayindir <levent@ceng.metu.edu.tr>) (still being developed)
Distrit 1.2 (Miguel Garvie <miguelgarvie@yahoo.co.uk>)
JavaWeather 1.0 (Serge Knystautas <sergek@users.sourceforge.net>)
JADE 7.0.8 (Jean-Marie Dautelle <jean-marie@dautelle.com>)
JFL 1.6.1 (Luke Reeves <luke@neuro-tech.net>)
Riaca Openmath 2.0rc2 (amc@win.tue.nl) (partial inclusion) (still being developed)
Swift FITS 1.3 (Edward A. Pier <eap@milkyway.gsfc.nasa.gov>)
Prover 1.0 (Greg Bush <GBush@HealthMarket.com>)
PSOL 1.0 (Kyle Siegrist <siegrist@math.uah.edu>)
RngPack 1.1a (Paul Houle <paul@honeylocust.com>)
Volrend 1.0 (Doug Gehringer <Doug.Gehringer@sun.com>)
GedML 1.0 (Michael H. Kay <mhkay@iclway.co.uk>)
NTP 1.0 (Michel Van den Bergh <vdbergh@luc.ac.be>)
JIGCELL 6.0.1 (tyson@vt.edu) (http://cdc25.biol.vt.edu/) (very partial inclusion) (still being developed)
JUMBO 4.6 (very partial inclusion) (http://almost.cubic.uni-koeln.de/cdk/cdk_top) (still being developed)
BrailleTrans 1.0 (Alasdair King <alasdairking@yahoo.co.uk>)
GML4J 1.0.3 beta (Aleksandar Milanovic <amilanovic@users.sourceforge.net>)
TigerAPI 1.8.2 (CVS release) (hajokeffer@users.sourceforge.net) (still being developed)
SEDRIS SRM 4.0 (se-mgmt@sedris.org) (still being developed)
Sigmakee 1.1 (Adam Pease <apease@users.sourceforge.net>)
Meson Calendar 1.0 applet (Mark E. Shoulson <mark@kli.org>)
GPSLIB4J 0.0.2 (tkuebler@users.sourceforge.net) (based on Henrik Aasted Sorensen's dk.itu.haas.GPS library)
J3d.org CVS source tree (resources@j3d.org) (very partial inclusion) (still being developed)
JDictClient 0.1.5 (bechynsk@users.sourceforge.net)
Generation5 JDK 27/12/04 (generation5@users.sourceforge.net) (still being developed)
Geotools 2.0 (desruisseaux@users.sourceforge.net) (very partial inclusion) (still being developed)
Itislib 1.0.0 (jones@nceas.ucsb.edu)
Aerodynamics 20/11/04 (Joe Huwaldt <jhuwaldt@mac.com>) (partial inclusion)
1976 Standard Atmosphere Program 1.3.1 (Joe Huwaldt <jhuwaldt@mac.com>) (partial inclusion)
JGraphT 0.6.0 (perfecthash@users.sf.net) (still being developed)
JHLabs image filters 01/01/05 (Jerry Huxtable <jerry@jhlabs.com>)
AudioStudio 1.1 (ftp://ftp.prenhall.com/pub/ptr/professional_computer_science.w-022/digital_audio/, craigl@att.net)
VirtualPlants 1.0 (rene@gressly.ch)
Mantissa 5.8 (Luc Maisonobe <luc@spaceroots.org>) (still being developed)
SelectionEngine alpha version (baylor@users.sourceforge.net)
Kaleido 1.0 (rl@math.technion.ac.il)
Computational Geometry in C (Second Edition) (orourke@cs.smith.edu)
Astrolib 1.1.4 (mark@mhuss.com) (partial inclusion)
Kojac 1.0 (ripoll@sony.de)
Antelmann 05/05 (info@antelmann.com) (partial inclusion)
QuantumJ 30/08/04 (myjinic@dev.java.net)
Nukesim 1.2 (dale@visser.name)
HMMPack 1.2 (troy.mcdaniel@asu.edu)
JGimmick 1.0 (part of JTEM) (jtem@sfb288.math.tu-berlin.de)
JGCL 1.0 (ryo@sra.co.jp)
ADFC 2.1 (adfcmh@users.sourceforge.net)
K-Means Clustering Module 1.0 (harlan@nyc.rr.com)
ISBNTools 01/01/06 (http://isbntools.com/)
StringSearch 1.2 (johann@johannburkard.de) (http://johannburkard.de/)
Flanagan 01/02/06 (m.flanagan@ee.ucl.ac.uk) (partial inclusion) (still being developed)
JShop2 1.0.2 (shop@cs.umd.edu) (still being developed)
GreenLightDistrict 1.3.1 (http://stoplicht.sourceforge.net/) (still being developed)
dk.brics.automaton 1.8-6 (amoeller@brics.dk) (http://www.brics.dk/automaton/) (still being developed)
JSpasm 1.1 (codexombie@users.sourceforge.net) (still being developed)
Jeops 2.1.1 (csff@cin.ufpe.br)
Form processing API 2.0 (iostro@sympatico.ca)
Elliptic functions 1.0 (part of JTEM) (jtem@sfb288.math.tu-berlin.de)
BLAS 1.0 (part of JTEM) (jtem@sfb288.math.tu-berlin.de)
Sputnik 2.1.1 (hme@chiandh.me.uk) (still being developed)
jlesa 0.3.0-rc4 (eknagy@omikk.bme.hu)
Observation Manager 1.4 (doergn@gmx.de) (still being developed)
NORAD Propagation Models in Java (jSGP) alpha-1 (mfunk@users.sourceforge.net)
VLE 0.2 (syvaidya@yahoo.com)
Skyview 2.3b (lmm@milkyway.gsfc.nasa.gov) (partial inclusion) (still being developed)
Prefuse beta 2007.10.21 (jheer@users.sourceforge.net) (partial inclusion) (still being developed)
Circuit applet 2007.12 (pfalstad@gmail.com)
PBF 2007.12 (A.D.Preece@cs.cardiff.ac.uk)


8. EPISTEME MARKET SHARE:

After a couple of years browsing the Web for Java code, and after reviewing thousands of packages and applets, I have looked at about everything that exist in Java about science, robots and 3D. From this work I can make a few comments and draw a picture of the Java world. Also looking at the global community I can make a couple of comments about the share of the Java code in the scientific software market.

First, it must be said that there is a lot of Java code as scientific projects, either as full libraries or short snippets. The opportunity applets offer for visual demonstrations is really a very important language design choice, but also Java appears to be interesting because it is a nice language to learn and test on which you are rapidly productive. This is appealing for scientists. But Java is also chosen for more mature projects: object oriented features, multi platform and ease of networking to a lesser extent are good reasons to go for a development. Finally, the plug and play concept of API makes code sharing very fruitful.

As we find more and more Java projects, we move away from Fortran code which was widely accepted for high end scientific computation before. Actually, the only viable competitor to Java today is C (and C++). From my personal experience I would say there is about one third of projects in Java and the other two third in C (this does not take into account commercial products which are all designed in C). But Java code quality is often awesome and there is much less people spending there time in developing code that has been developed elsewhere. So, overall Java is probably on par or beats C. Also, there is virtually no C#, and I think this is really good for everyone.

Java is not seen anymore as a slow system and computation intensive tasks are also programmed in Java. C, however, is largely favored for such tasks. Java has only one drawback, it is designed by a commercial company and not an international standard like C. Actually, I would even say that Java is in its golden age but will probably leave it once for all in a couple of years.

Who is using scientific computation ?

We have the following categories of scientific software products available:
- applet demos and hands on
- one time models for scientific publication
- university API and opensource community projects
- high end all language systems, mostly parallel, closed and governmental
- windows all purpose mathematics applications
- stand alone commercial tailored solutions with proprietary model

Applets and one time models are usually designed in universities, by young enthusiasts (or sometimes Java addicts). They are normally short pieces of code that the author drops there without a chance for reuse or improvement. Sometimes the one and only algorithm is reused in bigger projects, in the same or a different language. This is either a university API or a governmental closed project. Universities prefer Java while government agencies prefer C because a good engineer is less expensive than a supercomputer. They also are dependent on tradition, power and habits. It is in this kind of projects that we find some Fortran code. As their projects are very big, there are always some spin off libraries developed that compete with stand alone API efforts made by universities. But comparison shows that Java products offer more features, are better maintained and supported by the whole free software community.

Finally, there is a really different range of scientific product, namely industrial products. These are sold, offering proprietary models, source code is unavailable, API and extensions are rather poor. The user is therefore the "final" user. These products are side by side to the opensource Java API. Researchers routinely use both while people in the industry only use these close products. The reason is that researchers need to achieve some (one time) results whereas industrials are part of a work flow with furnishers and clients (ie: they are specialists that process data in a standard repeatable manner). Closed products are almost all written in C and have features that no API will ever compete with. They make few use of (possibly underlying) libraries but widgets. They come in two flavors: wide spectrum all purpose products like Mathematica, Mapple, Gauss and task specific for example crystallography software. The later class is often sold with a physical device used for data acquisition.

So, what is Episteme for ?

Episteme is a kind of Wikipedia for scientific software. It is less detailed than a book that would cover a specific area although, on the other hand it provides general guidelines and ideas about how things goes. Developers are welcomed to subclass the existing packages and provide some detailed implementation in an domain with the benefit of having some others developers doing the same in other domains. They also are guarantied of having standard code, tested and optimized algorithms for the well known scientific concepts taught at undergraduate level.

This should bring in some more researchers and universities projects on a short time scale. This won't hurt much industrial C coded applications but may be push the market a bit towards more inter operability between their closed models.

With computer evolving according to Moore law, scientific computation has a bright future. There is a need for a standard base of code no one can afford to develop anymore while testing more complex models.


9. RELEASE HISTORY:

In November 2003, I (Silvere Martin-Michiellot) was looking for a general Newton kinematics model for the Digital Biosphere project (a 3D realtime simulation of the earth for augmented reality, www.digitalbiosphere.org) when I found JSci (jsci.sourceforge.net) as a general purpose scientific API. Yet it provided only scientific support in mathematics, some physics and chemistry as well as some AWT and Swing components. I started to write new modules for JSci that could latter be used by Digital Biosphere rather than hacking them directly. As it turned out, Mark Hale, the JSci leader never accepted me fully as a developer of new packages which he always refused to commit to cvs. I wasn't so much bothered though, since I had my own projects and hoped to release my code elsewhere if needed.

By April 2004 I had been working about 80 hours a week on this and contacted tens of people who had realtime datasources or scientific projects I could repackage. One of these was Jean-Marie Dautelle, the author of JADE, that had a very nice Units package and was a competitor for the Mathematics package. From April until August I kept working on the project but at a very low pace. By that time, Jean-Marie couldn't keep the name JADE for his project for silly copyright issues. I offered him to merge our works into a new project we named Episteme. He managed to build up the website, register on dev.java.net and moved his original mature working code for release while I started working on the "experimental release", a merger of JSci, JADE and my code along with more and more packages up to cover the scientific knowledge up to undergraduate degree in every area.

Once again, from August 2004 to end 2005, I worked on week ends and late nights (although at a reasonable rate of about 20 hours a week), tried out hundereds of Java packages (actually almost every existing Java package) and contacted hundreds of people. From 2006 until now, I worked at a much lower pace as I found a job.

The result of all this is something that is about 4400 classes and 40 MB when zip-distributed with data files. This is the widest, most accurate and easiest scientific API ever. Many thanks to all countless contributors. Much remains to be done, actually there is work to improve every package for years.

Actually, after a second thought, I dreamt of having such a general science package about 20 years ago. Although, by that time there was no Java, and in fact no object language at all (apart from some lab tools). I am therefore especially happy of participating in such a project... and sad at the same time that noone tried hard enough before.

June/July 2008, first expected complete beta release.


10. SELECTED READINGS OR SOURCES:

http://www.google.com/ (and http://scholar.google.com)
http://www.wikipedia.org/
http://hyperphysics.phy-astr.gsu.edu/hbase/hframe.html
http://scienceworld.wolfram.com/
http://planetmath.org/encyclopedia
http://www.sciencesbookreview.com/
http://phrontistery.50megs.com/
http://www.cartage.org.lb/en/themes/Sciences/mainpage.htm
http://www.bartleby.com/reference/
http://i-cias.com/e.o/index.htm

Richard Phillips Feynman, Lectures on Physics: http://www.amazon.com/exec/obidos/tg/detail/-/0201021153/002-2161498-8708867?v=glance

Peter H. Raven, George B. Johnson, Biology: http://www.amazon.com/exec/obidos/ASIN/0073031208/qid=1077418580/sr=2-1/ref=sr_2_1/002-2161498-8708867

Duward F. Shriver, P.W. Atkins, Inorganic Chemistry: http://www.amazon.co.uk/exec/obidos/ASIN/019850330X/ref=pd_bxgy_text_2_cp/026-9525467-3130854 

Atkins, Julio de Paula, Julio De Paula, Atkins' Physical Chemistry, 7th Ed.: http://www.amazon.co.uk/exec/obidos/ASIN/0198792859/ref=lm_lb_6/026-9525467-3130854

David Harvey, Modern Analytic Chemistry: http://www.amazon.com/exec/obidos/tg/detail/-/0072375477/qid=1078529554//ref=sr_8_xs_ap_i1_xgl14/104-7144772-2507901?v=glance&s=books&n=507846

You may also find free scientific articles here:
International Network for the Availability of Scientific Publications http://www.inasp.info/peri/free.html
Free access litterature http://www.scidev.net/ms/open_access/index.cfm?pageid=169
HighWire http://highwire.stanford.edu/lists/largest.dtl
lanl.arXiv.org e-Print archive mirror http://xxx.lanl.gov/

Databases:
NIST Scientific and Technical Databases - Index: http://www.nist.gov/srd/
Welcome to the NIST WebBook: http://webbook.nist.gov/
Sciences Databases: http://www.lib.uchicago.edu/e/crerar/db/
GeoHive: http://www.xist.org/default1.aspx


11. JAVA SPECIFIC LINKS:

The links below are a list to third party Java libraries and XML data formats. It should help you to cover the areas not directly (or partly covered) by this library.

Frameworks for applications:
OPA http://sourceforge.net/projects/openprintassist/
Java Plugin Framework http://jpf.sourceforge.net/

Installers:
Yagga http://www.yagga.net/java/miniinstaller/
Jifi http://www.openinstallation.org/ (born out of JSR38)
IzPack http://www.izforge.com/izpack/index.php?page=home

Autoupdater:
http://sourceforge.net/projects/autoupdater

Caches packages:
JSR 107: JCACHE - Java Temporary Caching API http://jcp.org/en/jsr/detail?id=107
FKache http://jcache.sourceforge.net/
Commons Cache http://jakarta.apache.org/commons/sandbox/cache/
Java Caching System http://jakarta.apache.org/turbine/jcs/
BioJava http://www.biojava.org/

Resources for teachers (very short list, see http://en.wikipedia.org/wiki/Learning_Management_System for more links):
APIS QTIv2 Assessment Engine http://sourceforge.net/projects/apis
Online Learning And Training http://www.olat.org/
ActiveMath http://www.activemath.org/


12. SCIENCE PACKAGE SPECIFIC LINKS:

The links below are a list to third party Java libraries and XML data formats. It should help you to cover the areas not directly (or partly covered) by this library. If you really need a very exhaustive list of links, look into javascientificpackages.html (in this directory).
You will also find a good list of Java API at http://java-source.net/ and http://www.manageability.org/blog/opensource/.

Architecture:
-

arts:
-

Astronomy:
Hipparcos and Tycho Catalogues http://astro.estec.esa.nl/Hipparcos/catalog.html
Tycho2 http://geocities.yahoo.com.br/astrobhbr/tycho2.htm
Comet/asteroid Orbit Determination and Ephemeris Software http://home.earthlink.net/~jimbaer1/
Java Astrodynamics Toolkit http://jat.sourceforge.net/
JSky http://archive.eso.org/JSky/

Awt:
See below, Swing.

Bibliography:
Google API http://www.google.com/apis/index.html

Biology:
Biojava http://www.biojava.org/
PAL: Phylogenetic Analysis Library http://www.cebl.auckland.ac.nz/pal-project/
Mesquite http://mesquiteproject.org/mesquite/mesquite.html

Chemistry:
CML, Jumbo http://www.xml-cml.org/jumbo3/ 
JMol http://jmol.sourceforge.net/
Marvin http://www.chemaxon.com/marvin/
CDK http://cdk.sourceforge.net/index.html
JChemPaint http://jchempaint.sourceforge.net
JEFF-3.0 http://www.nea.fr/html/dbdata/projects/nds_jef.htm
JANIS http://www.nea.fr/janis/welcome.html
Webreactions http://webreactions.net/index.html
JCrystal http://jcrystal.com/index.html
MBT http://mbt.sdsc.edu/docs/install.html

Computing:
See corresponding directory (org.episteme.computing and org.episteme.computing.ai) and
JSR94 http://www.jcp.org/en/jsr/detail?id=94 (see http://java-source.net/open-source/rule-engines)
JSR73 http://www.jcp.org/en/jsr/detail?id=73
JSR247 http://jcp.org/en/jsr/detail?id=247
JavaParty http://wwwipd.ira.uka.de/JavaParty/
JHMM http://www.milowski.com/software/jhmm/
JHDL http://www.jhdl.org/

Devices:
http://www.raben.com/articles/TelescopeInterface/resources.html

Earth:
XMML https://www.seegrid.csiro.au/twiki/bin/view/Xmml/WebHome
Grad https://sourceforge.net/projects/grad/

Economics:
financial XML http://www.finxml.org/
Jpos http://www.jpos.org/
JDollars http://jdollars.sourceforge.net/
Real-Time Data Feed API http://www.modulusfe.com/api/default.asp
Barcode generator http://sourceforge.net/projects/barbecue/
JASA http://jasa.sourceforge.net/
XBRL http://www.xbrl.org/
XBRL Library http://www.batavia-xbrl.com/index2.html?tothesite=products+and+services
HR-XML http://www.hr-xml.org/
Ontology Management System http://www.alphaworks.ibm.com/tech/snobase
WebCabComponents http://www.webcabcomponents.com/
jquantlib http://www.jquantlib.org/sites/jquantlib/
jfin http://jfin.org/wp/

Engineering:
J-Sim http://www.j-sim.zcu.cz/
JiST http://jist.ece.cornell.edu/

Geography:
Geotools http://www.geotools.org/
GeoAPI http://geoapi.sourceforge.net/
Openmap http://openmap.bbn.com/

Help:
-

History:
JODA Time http://joda-time.sourceforge.net/
Time Package http://www.dprism.com/products/time/features.html 
HEML http://www.heml.org/
JTemporal http://jtemporal.sourceforge.net/

Io:
-

Law:
LegalXML http://www.legalxml.org/

Linguistics:
Chineese encoding converter http://www.mandarintools.com/javaconverter.html
JWNL http://jwordnet.sourceforge.net/
Lucene http://lucene.apache.org/java/docs/index.html
Jazzy http://jazzy.sourceforge.net/
dk.brics.grammar http://www.brics.dk/~amoeller/grammar/
Jena http://jena.sourceforge.net/
https://open-language-tools.dev.java.net/
(may be I should include more about RDF, DAML+OIL, OWL)

Mathematics:
Geometry http://mathsrv.ku-eichstaett.de/MGF/homes/grothmann/java/zirkel/doc_en/
JavaMath API - Welcome http://javamath.sourceforge.net/
Qedeq http://www.qedeq.org/
Orbital http://www.functologic.com/orbital/index.html
Visual Numerics - Developers of IMSL and PV-WAVE http://www.vni.com/products/imsl/jmsl.html
Java Tools for Experimental Mathematics http://www-sfb288.math.tu-berlin.de/~jtem/
Vecmath https://vecmath.dev.java.net/
Stochastic Simulation in Java http://www.iro.umontreal.ca/~lecuyer/ssj/
Javaview http://www.javaview.de/
JMatLink http://sourceforge.net/projects/jmatlink
JSCL Meditor http://jscl-meditor.sourceforge.net/
JTS http://www.vividsolutions.com/jts/jtshome.htm
JStatCom http://www.jstatcom.com/javadoc/index.html
Colt http://dsd.lbl.gov/~hoschek/colt/
MPJava http://www.cs.unc.edu/Research/HARPOON/mpjava/

Measure:
http://jcp.org/en/jsr/detail?id=275

Media:
See org.episteme.computing.ai (vision, image processing links) and
Java Advanced Imaging http://java.sun.com
Java Media Framework http://java.sun.com
Java3D http://java.sun.com
JMusic http://jmusic.ci.qut.edu.au

Medicine:
NeuroML http://www.neuroml.org/
MorphML http://germain.umemat.maine.edu/faculty/crook/MorphML.html
CellML http://www.cellml.org/
BrainML http://www.brainml.org/
AnatML http://www.physiome.org.nz/anatml/
NeatMed http://www.eeng.dcu.ie/%7Evsl/DICOM/
SPLViz http://splweb.bwh.harvard.edu:8000/pages/projects/vizualization/splviz/splviz.html
Bio-medical Imaging in Java http://bij.isi.uu.nl/index.htm
IASO http://iaso.cit.nih.gov/

Ml:
-

Net:
-

Philosophy:
-

Physics:
FreeHEP http://java.freehep.org/index.html
ODE for Java https://odejava.dev.java.net/
ODE for Java (another implementation) http://sourceforge.net/projects/odeforjava/

Politics:
-

Psychology:
Modular Reasoning System http://www.ksl.stanford.edu/software/JTP/

Sociology:
OASIS HumanMarkup http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=humanmarkup
HR-XML http://www.hr-xml.org/

Sports:
-

Swing:
General link resource http://www.javadesktop.org/rollups/components/index.html
Jun4Java http://www.sra.co.jp/people/nisinaka/Jun4Java/index_en.html
Batik http://xml.apache.org/batik/index.html
JGraph http://www.jgraph.com/
OpenJGraph http://openjgraph.sourceforge.net/
YFiles http://www.yworks.com/en/products_yfiles_about.htm
Visad http://www.ssec.wisc.edu/~billh/visad.html
Scientific Graphics Toolkit http://www.epic.noaa.gov/java/sgt/
Many organizations provide charting packages (open source only below):
JFreeChart, JFreeReport http://www.jfree.org/
JCharts http://jcharts.sourceforge.net/index.html
UJAC http://ujac.sourceforge.net/index.html
PtPlot http://ptolemy.eecs.berkeley.edu/java/ptplot/
Chart2D http://chart2d.sourceforge.net/
JopenChart http://jopenchart.sourceforge.net/
Thundergraph http://sourceforge.net/projects/thundergraph
Jchart2d http://jchart2d.sourceforge.net/
Java Charts, JVCL, LwVCL http://www.zaval.org/products/index.html
JRobin http://www.jrobin.org/

Tests:
JUnit http://www.junit.org/
Log4j http://logging.apache.org/log4j/docs/index.html

Text:
-

Util:
Jakarta commons http://jakarta.apache.org/commons/
Ostermiller http://ostermiller.org/utils/
Antelmann http://www.antelmann.com/developer/index.html
Mindprod http://www.mindprod.com/
Jodd http://jodd.sourceforge.net/


13. CONTACT US:

Though we do not have any formal postal address, you can contact us using the website jade.dev.java.net, www.episteme.org, or episteme.dev.java.net. You can also join the mailing list or contact directly:
"Silvere Martin-Michiellot" <silvere@digitalbiosphere.org> (for the Episteme EXPERIMENTAL library only)
or "Jean-Marie Dautelle" <jean-marie@dautelle.com> (for the Episteme OFFICIAL library only)
