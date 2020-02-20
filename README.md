
<p><strong>Universit&agrave; degli studi di Camerino</strong></p>
<p>&nbsp;</p>

<p>Documentazione progetto Business Process Digitalization and Cloud Computing</p>

<h2>Your online repository for .bpmn models<h2>
<strong>Abstract</strong>
  <h3>
<p>The project consists of a web platform which allows users to register and access the different .bpmn files uploaded to the platform repositories. Briefly, each registered user has a private repository accessible only to that user, while he can have access to all public repositories also of other users. Within a repository the user can create a .bpmn model, create folders, delete model or move folders.</p>
<p>The user can use both collaboration and choreography model within his repositories.</p>
<p>For every model the user can create different versions, then work on the desired version in order to have a history of the changes made to the same .bpmn model. User can open the desired version of the .bpmn file in the bpmn.io editor built-in inside the platform, or download directly this version, or a collection of versions for the same model, to his local machine. The user can check the validity and soundness of the model and see the results shown (available only for collaboration model) (actually not available in online view).</p>
<p>The registered user will have at his disposal a search-bar to search for a bpmn model in a repository and will be able to share his own models with other users.</p>
<p>User can use &ldquo;C4 TOOL &ndash; COLLABORATION VS CHOREOGRAPHY CONFORMANCE CHECKING IN BPMN 2&rdquo;, <strong>&nbsp;</strong>the C4 formal framework aims at supporting modelers to automatically check whether a collaboration conforms to a prescribed choreography.</p>
<p>It&rsquo;s also possible to join two differents collaboration models using &ldquo;merge&rdquo; option. The tool allows you to join two different models (like sender and receiver), connecting the right&nbsp; matching message flow, if they are.</p>

<h2><strong>Installation (on local machine)</strong></h2>
<p>Before downloading the project you need to prepare your local machine: for client-side project part Node.js is needed; instead for the server-side part you need to install Eclipse IDE for Java EE Developers.</p>
<p>Since the frontend part of the project was made with Angular, to start it you will need Node.js; while the server part is built using Java Spring WebFlux with Maven and then you'll need Java EE IDE to run the server. Now you can clone or download our project in GitHub repository.</p>
<p>&nbsp;Inside the project you will find two main subfolders, one is called SPMClient and contains all the client-side files; instead the other folder SPMServer contains server-side files.</p>
</h3>
<p>&nbsp;</p>
<p>&nbsp;</p>
<h2><strong>Usage (on local machine)</strong></h2>
<p>If you want to use this framework on your machine, you have to set some parameters.</p>
<p>For the client, you must go in &ldquo;package.json&rdquo; file and change the &ldquo;start&rdquo; command.</p>
<p>If you want to run it, you have to change the command in</p>
<p>&nbsp;&ldquo; ng serve --port &ldquo;yourPort&rdquo; --host&nbsp; &ldquo;yourIpAddress&rdquo;&nbsp; (personalize settings) or</p>
<p>&ldquo;ng serve&rdquo; (default settings, localhost:4200)</p>

<a href="https://ibb.co/kJQynKj"><img src="https://i.ibb.co/YXpW92m/image.png" alt="image" border="0"></a><br/>
<p>For the server, in the &ldquo;CorsConfig.java&rdquo; file, you have to change the variable &ldquo;ALLOWED-ORIGIN&rdquo;.</p>
<p>Obviously you have to set the same client&rsquo;s&nbsp; ip address and port.</p>

<a href="https://ibb.co/3zFLxDd"><img src="https://i.ibb.co/zJ203yx/image.png" alt="image" border="0"></a><br/>

<p>Now to run the project it is necessary to simultaneously execute the client part and the server part:</p>
<ul>
<li><strong>Run SPMClient:</strong> first at level Project-folder\SPMClient you need to execute the command npm install and then npm start. Once npm start is finished, usually an Angular Live Development Server will listen on &ldquo;ipAddress:port-number&rdquo;. If you try to use the platform now, it won't work because the server is offline.</li>
</ul>
<p>&nbsp;</p>
<p>&nbsp;</p>
<ul>
<li><strong>Run SPMServer:</strong> Open Java EE IDE, then in the top menu click on File, then Import, then Existing Maven Project choosing as Root Directory the folder SPMServer and putting the check on the suggested pom.xml file. Click on finish and wait for the project to be completely imported. This can require several minutes and you will notice the process looking at the bar on the right bottom. Once the project is ready, go to Project Explorer and right click on the root folder SPMServer, then Run As -&gt; Spring Boot App. When the server starts now you can return to your browser where the client part is opened.</li>
</ul>
<p>&nbsp;</p>
<p>&nbsp;</p>
<h2><strong>Online usage </strong></h2>
<p>To use the online platform you need to connect to the link <a href="http://proslab.unicam.it:4900/login">http://proslab.unicam.it:4900/</a></p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<h2><strong>&nbsp;</strong></h2>

