<html>
<head>
<meta charset="utf-8">
<title>$SECTION_NAME$ - $SITE_NAME$</title>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
<?$META_DESCRIPTION$?>
<?$META_KEYWORDS$?>
<script type="text/javascript">
		var browser			= navigator.userAgent;
		var browserRegex	= /(Android|BlackBerry|IEMobile|Nokia|iP(ad|hone|od)|Opera M(obi|ini))/;
		var isMobile		= false;
		if(browser.match(browserRegex)) {
			isMobile			= true;
			addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false);
			function hideURLbar(){
				window.scrollTo(0,1);
			}
		}
</script>
<link type="text/css" rel="StyleSheet" href="/_st/my.css" />
<script type="text/javascript">
var navTitle = 'Navigation';
</script>

</head>

<body>
$ADMIN_BAR$
<div id="overlay">
 <div id="top-p">
  <div class="wrapper">
  	<header>
  		$GLOBAL_AHEADER$
        $GLOBAL_PROMO$        
        <nav>
        	<div id="nav-box">
        	 <div id="nav-w">
              <div id="catmenu">
               <!-- <sblock_nmenu> -->
<?if($NMENU_1$)?><!-- <bc> -->$NMENU_1$<!-- </bc> --><?endif?>
<!-- </sblock_nmenu> -->
              <div class="clr"></div>
              </div>
             </div>
        	</div>
            <div id="nav-sep"></div>
        </nav>
  	</header>
    <div id="casing">
         <?if($MODULE_ID$='forum')?><div class="forum-box"><?endif?>
         <!-- <middle> -->
         <div id="content" <?if($HIDE_CLEFTER$)?>class="wide-page"<?endif?>>
           <section><!-- <body> --><div><a href="$HOME_PAGE_LINK$"><!--<s5176>-->Home<!--</s>--></a> &raquo; $SECTION_NAME$</div>
<hr />
<?if($MODULE_SELECTOR$)?>$MODULE_SELECTOR$<hr /><?endif?>
<table border="0" cellpadding="0" cellspacing="0" width="100%"><tr>
<td width="60%"><!--<s5246>-->Comments found<!--</s>-->: <b>$NUM_ENTRIES$</b><?if($NUM_SHOWN$)?><br /><!--<s5247>-->Comments displayed<!--</s>-->: <b>$NUM_SHOWN$</b><?endif?></td>
<td align="right"><?if($PAGE_SELECTOR$)?><!--<s3015>-->Pages<!--</s>-->: $PAGE_SELECTOR$<?endif?></td></tr></table><hr />
$BODY$
<?if($PAGE_SELECTOR1$)?><div style="text-align:center;">$PAGE_SELECTOR1$</div><?endif?><!-- </body> --></section>
          </div>
          <?if(!$HIDE_CLEFTER$)?>
          <aside>
         <div id="sidebar">  
            $GLOBAL_CLEFTER$
         </div>
        </aside> 
        <?endif?>     
         <!-- </middle> -->
         <div class="clr"></div>
         <?if($MODULE_ID$='forum')?></div><?endif?>
    </div>
  </div>
 </div>
 <div id="bot-p">
  <footer>
  	<div class="wrapper">
  	 <div id="footer">
      <div id="foot-w"></div>
        <div id="foot-i">
         <div class="foot-l"><!-- <copy> -->Copyright MyCorp &copy; $YEAR$<!-- </copy> --></div>
         <div class="foot-m">
          <div id="fsoc-box">
              <?if($RSS_LINK$)?><a href="$RSS_LINK$" class="soc-rs"></a><?endif?>
              <a href="https://www.facebook.com/" class="soc-fc"></a>
              <a href="https://twitter.com/" class="soc-tw"></a>
              <a href="http://www.flickr.com/" class="soc-fl"></a>
              <a href="https://vimeo.com/" class="soc-vi"></a>
          </div>
         </div>
         <div class="foot-r">$POWERED_BY$</div>
         <div class="clr"></div>
        </div>
     </div>
  	</div>
  </footer>
 </div>
</div>
<script>
  var sw = $(window).width();
breakpoint = 979,
mobile = false;
if (sw < breakpoint) {
    $('#catmenu').attr('class', 'nav-mobi')
}
</script>
<script type="text/javascript" src="/.s/t/1211/ui.js"></script>

<div style="display:none">

</div>
</body>
</html>
