<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1">
  <title>Fancytree - Example</title>

  <script src="../fancytree/lib/jquery.js"></script>
  <script src="../fancytree/lib/jquery-ui.custom.js"></script>

  <link href="../fancytree/src/skin-win8/ui.fancytree.css" rel="stylesheet">
  <script src="../fancytree//src/jquery.fancytree.js"></script>

  <!-- (Irrelevant source removed.) -->

 
<script type="text/javascript">
  $.ui.fancytree.debugLevel = 1; // silence debug output

  function logEvent(event, data, msg){
//        var args = $.isArray(args) ? args.join(", ") :
    msg = msg ? ": " + msg : "";
    $.ui.fancytree.info("Event('" + event.type + "', node=" + data.node + ")" + msg);
  }
  $(function(){
    $("#tree").fancytree({
      //checkbox: true,
      // --- Tree events -------------------------------------------------
      blurTree: function(event, data) {
        logEvent(event, data);
      },
      create: function(event, data) {
        logEvent(event, data);
      },
      init: function(event, data, flag) {
        logEvent(event, data, "flag=" + flag);
      },
      focusTree: function(event, data) {
        logEvent(event, data);
      },
      restore: function(event, data) {
        logEvent(event, data);
      },
      // --- Node events -------------------------------------------------
      activate: function(event, data) {
        logEvent(event, data);
        var node = data.node;
        // acces node attributes
        $("#echoActive").text(node.title);
        if( !$.isEmptyObject(node.data) ){
//          alert("custom node data: " + JSON.stringify(node.data));
        }
      },
      beforeActivate: function(event, data) {
        logEvent(event, data, "current state=" + data.node.isActive());
        // return false to prevent default behavior (i.e. activation)
//              return false;
      },
      beforeExpand: function(event, data) {
        logEvent(event, data, "current state=" + data.node.isExpanded());
        // return false to prevent default behavior (i.e. expanding or collapsing)
//        return false;
      },
      beforeSelect: function(event, data) {
//        console.log("select", event.originalEvent);
        logEvent(event, data, "current state=" + data.node.isSelected());
        // return false to prevent default behavior (i.e. selecting or deselecting)
//        if( data.node.isFolder() ){
//          return false;
//        }
      },
      blur: function(event, data) {
        logEvent(event, data);
        $("#echoFocused").text("-");
      },
      click: function(event, data) {
    	  $("#div1").load("gisa.html");
        logEvent(event, data, ", targetType=" + data.targetType);
        // return false to prevent default behavior (i.e. activation, ...)
        //return false;
      },
      collapse: function(event, data) {
        logEvent(event, data);
      },
      createNode: function(event, data) {
        // Optionally tweak data.node.span or bind handlers here
        logEvent(event, data);
      },
      dblclick: function(event, data) {
        logEvent(event, data);
//        data.node.toggleSelect();
      },
      deactivate: function(event, data) {
        logEvent(event, data);
        $("#echoActive").text("-");
      },
      expand: function(event, data) {
        logEvent(event, data);
      },
      enhanceTitle: function(event, data) {
        logEvent(event, data);
      },
      focus: function(event, data) {
        logEvent(event, data);
        $("#echoFocused").text(data.node.title);
      },
      keydown: function(event, data) {
        logEvent(event, data);
        switch( event.which ) {
        case 32: // [space]
          data.node.toggleSelected();
          return false;
        }
      },
      keypress: function(event, data) {
        // currently unused
        logEvent(event, data);
      },
      lazyLoad: function(event, data) {
        logEvent(event, data);
        // return children or any other node source
        data.result = {url: "ajax-sub2.json"};
//        data.result = [
//          {title: "A Lazy node", lazy: true},
//          {title: "Another node", selected: true}
//          ];
      },
      loadChildren: function(event, data) {
        logEvent(event, data);
      },
      loadError: function(event, data) {
        logEvent(event, data);
      },
      modifyChild: function(event, data) {
        logEvent(event, data, "operation=" + data.operation +
          ", child=" + data.childNode);
      },
      postProcess: function(event, data) {
        logEvent(event, data);
        // either modify the ajax response directly
        data.response[0].title += " - hello from postProcess";
        // or setup and return a new response object
//        data.result = [{title: "set by postProcess"}];
      },
      renderNode: function(event, data) {
        // Optionally tweak data.node.span
//              $(data.node.span).text(">>" + data.node.title);
        logEvent(event, data);
      },
      renderTitle: function(event, data) {
        // NOTE: may be removed!
        // When defined, must return a HTML string for the node title
        logEvent(event, data);
//        return "new title";
      },
      select: function(event, data) {
        logEvent(event, data, "current state=" + data.node.isSelected());
        var s = data.tree.getSelectedNodes().join(", ");
        $("#echoSelected").text(s);
      }
    }).bind("fancytreeactivate", function(event, data){
      // alternative way to bind to 'activate' event
//        logEvent(event, data);
    }).on("mouseenter mouseleave", ".fancytree-title", function(event){
      // Add a hover handler to all node titles (using event delegation)
      var node = $.ui.fancytree.getNode(event);
      node.info(event.type);
    });
  });
</script>
</head>

<body class="example">
  <h1>Example: Default</h1>
  <div class="description">
    This tree uses default options.<br>
    It is initialized from a hidden &lt;ul> element on this page.
  </div>
  <div>
    <label for="skinswitcher">Skin:</label> <select id="skinswitcher"></select>
  </div>
  <div id="tree">
    <ul id="treeData" style="display: none;">
      <li id="id1" title="Look, a tool tip!">item1 with key and tooltip
      <li id="id2">item2
      <li id="id3" class="folder">Folder <em>with some</em> children
        <ul>
          <li id="id3.1">Sub-item 3.1
            <ul>
              <li id="id3.1.1">Sub-item 3.1.1
              <li id="id3.1.2">Sub-item 3.1.2
              <li id="id3.1.3">Sub-item 3.1.3
            </ul>
          <li id="id3.2">Sub-item 3.2
            <ul>
              <li id="id3.2.1">Sub-item 3.2.1
              <li id="id3.2.2">Sub-item 3.2.2
            </ul>
        </ul>
      <li id="id4" class="expanded">Document with some children (expanded on init)
        <ul>
          <li id="id4.1"  class="active focused">Sub-item 4.1 (active and focus on init)
            <ul>
              <li id="id4.1.1">Sub-item 4.1.1
              <li id="id4.1.2">Sub-item 4.1.2
            </ul>
          <li id="id4.2">Sub-item 4.2
            <ul>
              <li id="id4.2.1">Sub-item 4.2.1
              <li id="id4.2.2">Sub-item 4.2.2
            </ul>
        </ul>
    </ul>
  </div>

  <!-- (Irrelevant source removed.) -->
  
  <div id="div1"></div>

</body>
</html>