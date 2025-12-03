Jscience is a general scientific library in Java.
- Actually, your basic survival kit as a developer looking for science -



This is the readme.txt file for JScience (http://www.jscience.org/) version 2.0 EXPERIMENTAL, as of 4th September 2007.

For a complete readme.txt file read the v1 readme.txt in the corresponding sub directory.


This is starting point for version 2.0 of the Mathematics package although the complete 1.0 version of JScience has not yet been released. Use at your own risks.


Expected first beta release hopefully... someday. 


All packages and data are delivered under GPL license EXCEPT a couple packages using specific licenses:
org.jscience.mathematics.geometry

Many of these licenses are quite similar to GPL which are usually called "compatible".

See license.txt in this directory and license.txt in corresponding directories for appropriate usage.


todo.txt gives a list of limitations and features we may work on.


Please read the WARNINGS section (section 5 of this file).

------------------------------------------------------------------------------------------

1. FEATURES
2. INSTALLATION FOR END USERS
3. COMPILATION AND JAVADOC FOR DEVELOPERS
4. REQUIRED LIBRARIES
5. WARNINGS
6. CONTRIBUTIONS
7. USED PACKAGES
8. CONTACT US

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

Why using JScience and not another package ? Well, JScience is a collective effort which has the aim to be maintained and enhanced over the time. JScience is also quite useful because you can submit your subpackages for addition into the framework for the benefits of everyone. Finally we make a lot of effort to always provide the fastest algorithms in a strong architecture, something that is quite difficult to achieve, especially for specialized packages.

This is an API, not an application per se. Mostly, it is an API to develop some others API or some applications. We therefore expect a different target audience than the mainstream internet user. In fact we target well defined sub groups:
- API builders to design APIs on top of this one
- Corporate developers looking for a wide spectrum free API
- Applet/Webstart developers that need to quick develop a product
- Researchers and graduate students that want to test something and (may be) don't require to build a full application

JScience, and especially the numerical intensive packages (mostly mathematics), uses two models. The first one is the traditional way in which scientific libraries are designed, is optimized for speed and relies heavily on ints and doubles, while offering a strong logic. The other one provides an even better model closer to theoritical concepts generally acknowledged in science. It is based on ExactIntegers and ExactReals which both provide infinite precision support at the sacrifice of some speed. It comes along with unit systems and quantities. It is much less developed in the current release than the traditional model mostly for the reason that it makes this API bigger and more complex while probably required by less users. There are some bridges that help switch from one model from another so you shouldn't have much difficulites using any of the two. Classes normally behave the same way in both models and mostly only differ by their names.

The library supports almost everything you should expect in Mathematics.

There are also some XML parsers, namely MathML.

Some limitations are to be mentionned, see todo.txt for a complete explanation.

Finally there are many data files that are cobundled along with the classes. They are support information, NOT necessarily used by classes but brought to you so that you can have a handy reliable source of data.

Although we have made many efforts to produce such a library there is still room for a lot of improvements. Please report any inaccuray, bug, feature request through the JScience website http://www.jscience.org/.


2. INSTALLATION FOR END USERS:

Apart from Java itself, the distribution contains all the required files to be ready to run.

Install Java (1.5 or later) from http://www.java.com/. All class files are Java VM 1.5 compatible (as far as we know) although most of the packages should compile with Java 1.3 if needed.
See also the "Required libraries" section below.

a. Unzip the current version of jscience.zip.

b. either:
Put the JScience library in the <jre>/lib/ext directory of your Java installation, removing all previous Jscience files.
Also include all the files in jscience/lib/ in the <jre>/lib/ext directory of your Java installation.
Note that you may write over existing files (which are probably older versions of the same libraries).
Or:
Remove any previous version of JScience by simply deleting the org.jscience directory.
Make sure your CLASSPATH environment variable contains a path to jscience/classes/jscience.jar.
Make sure your CLASSPATH environment variable contains a path to jscience/lib/ (the packages needed by JScience to run).

Run any sample with the command line, as usual.


3. COMPILATION AND JAVADOC FOR DEVELOPERS:

To compile all the code:
Install Java (1.5 or later) from http://www.java.com/. All class files are Java VM 1.5 compatible although most of the packages should compile with Java 1.3 if needed.
The text encoding for the compiler must be UTF-8 (mainly for some Measure package classes) except for the  org.jscience.mathematics.geometry (cp1252).
See also the "Required libraries" section below.
Compile with the command line, as usual or with Ant (http://ant.apache.org/) using the supplied build.xml in the org package.

Javadoc shouldn't be generated for some classes in org.jscience.mathematics.algebraic.numbers and org.jscience.mathematics.geometry see the readme.txt in the corresponding package, as these classes are not part of the API.


4. REQUIRED LIBRARIES:

To compile and/or run all the classes in this library you will also need several libraries (list below). These libraries have been included in the lib directory (apart from JavaComm and Java3D which you can download from Sun) so that you just need to drag and drop them to you Java /lib/ext folder.

The XML parsers (org.jscience.ml subpackages) require several XML libraries:
Xerces XML parser found at http://xml.apache.org/xerces2-j/
JDom XML parser found at http://www.jdom.org/
JAXEN XML parser found at http://www.jaxen.org/

Remember that you only need these libraries if you use the corresponding package or plan to recompile everything.

Optional libraries:
tools.jar (JDK doclet system) from the JDK lib directory must be in the classpath to compile the org.jscience.help.doclet subpackage. Warning, only version 1.5 of the tool.jar library (distributed with Java JDK 1.5) is supported, it won't compile with previous versions. This package is mostly needed if you want to recompile JScience and of no use as a developer.
JUnit for org.jscience.test subpackage found at http://www.junit.org/index.htm. This package is needed only if you want to test JScience and of no use as a developer.
Ant build tool to automate compilation found at http://ant.apache.org/

All these are also bundled in the lib directory along with the source code of JScience.

Note about the MathML 2.0, 2nd edition implementation and dtd:
The MathML (partial) implementation that can be found on the Jscience website is different from the standard implementation of the MathML dtd (http://dev.w3.org/cvsweb/java/classes/org/w3c/dom/mathml/) and corrects minor bugs. Please download the version from jscience named MathML DOM. You will find the MathML dtd at http://www.w3.org/TR/2003/REC-MathML2-20031021/

5. WARNINGS:

Though we made every possible effort to produce something that really rocks, we make no warranty of any sort about the results produced by using this library or whatsoever. You may actually loose zillions, crash your probe on Mars using the library which is provided as such and that you use at your own risk.

This is a scientific package. Futures release may NOT be compatible with the current code from the library. This means that code may NOT be upward compatible. One reason is that science changes from time to time and perhaps contradicts itself, wich means that we may have to rewrite the code because "it is not scientific anymore". However, we will make every effort so that there is very few effort on your side to update your code to a new version. Our philosophy is to produce a new version to provide more features, more precision, more efficiency, less bugs.

This is quite a large library. It is therefore full of errors, inconsistencies and innacuracies. Yelling is not the solution. Please, fill a request for enhancement, a bug report or drop us a mail for every missing contribution, copyright infringment or the like. We are working hard to make this library as good as possible and will make every effort to provide a rapid answer.


6. CONTRIBUTIONS:

We have asked and received permission to use all the code and files we did not produce ourselves.

We also bundle many datafiles and physical measurements produced by many sources. Some of these files were converted to xml or reorganized by ourselves. You will normally find an accompying document (Java, html or text file) describing the original location(s).

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
- you want to review or write some comments about JScience or one of the pacakges
- you have written an application or an applet that uses JScience and would like to let us know so that we can link to you

Don't hesitate to contact us should you want to contribute in a way or another.


7. USED PACKAGES:

This software is based on the work from several authors which code we used to produce our own code. Normally, we merge every feature from every package (a notable exception is Biojava).

We downloaded the code for (almost) each package on the 1st January 2005, so any later release should be more recent than the code we built JScience upon.

Specifically:
Janet 1.10 beta (partial inclusion) (Carsten Knudsen <Carsten.Knudsen@fysik.dtu.dk>)
JSci 0.93 (Mark Hale <m.hale@qmul.ac.uk>) (still being developed)
JADE 7.0.8 (Jean-Marie Dautelle <jean-marie@dautelle.com>)
JFL 1.6.1 (Luke Reeves <luke@neuro-tech.net>)
Riaca Openmath 2.0rc2 (amc@win.tue.nl) (partial inclusion) (still being developed)
Prover 1.0 (Greg Bush <GBush@HealthMarket.com>)
PSOL 1.0 (Kyle Siegrist <siegrist@math.uah.edu>)
RngPack 1.1a (Paul Houle <paul@honeylocust.com>)
Mantissa 5.8 (Luc Maisonobe <luc@spaceroots.org>) (still being developed)
Kaleido 1.0 (rl@math.technion.ac.il)
Computational Geometry in C (Second Edition) (orourke@cs.smith.edu)
Elliptic functions 1.0 (part of JTEM) (jtem@sfb288.math.tu-berlin.de)
BLAS 1.0 (part of JTEM) (jtem@sfb288.math.tu-berlin.de)
JGCL 1.0 (ryo@sra.co.jp)


8. CONTACT US:

Though we do not have any formal postal address, you can contact us using the website www.jscience.org, or jscience.dev.java.net. You can also join the mailing list or contact directly:
"Jean-Marie Dautelle" <jean-marie@dautelle.com>
"Silvere Martin-Michiellot" <silvere@digitalbiosphere.org>
