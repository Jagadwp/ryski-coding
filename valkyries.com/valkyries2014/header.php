<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><?php bloginfo('name'); ?></title>

<meta name="description" content="Official site for the New England Valkyries Rugby Football Club">
<meta name="viewport" content="width=device-width,initial-scale=1">

<meta property="og:image" content="<?php bloginfo('template_url'); ?>/images/valkyriestrans.png" />
<link rel="image_src" href="<?php bloginfo('template_url'); ?>/images/valkyriestrans.png" />

<script type="text/javascript" src="<?php bloginfo('template_url'); ?>/js/google.analytics.js"></script>
<script type="text/javascript" src="<?php bloginfo('template_url'); ?>/js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="<?php bloginfo('template_url'); ?>/js/jquery.highlightnav.js"></script>
<script type="text/javascript" src="<?php bloginfo('template_url'); ?>/js/jquery.twittertoggle.js"></script>
<script type="text/javascript" src="<?php bloginfo('template_url'); ?>/js/background.js"></script>

<link type="text/css" rel="stylesheet" href="<?php bloginfo('stylesheet_url'); ?>">
<link type="text/css" rel="stylesheet" media="(max-width: 568px)" href="<?php bloginfo('template_url'); ?>/mobile.css">

<link rel="shortcut icon" type="image/x-icon" href="<?php bloginfo('template_url'); ?>/images/favicon.ico">
<?php wp_head(); ?>
</head>
<body>
	<header>
		<div id="favorites">
			<ul>
				<li><a href="mailto:ValkyriesRFC@gmail.com" title="email us!"><img
						id="mailicon" src="<?php bloginfo('template_url'); ?>/images/mail-icon.png"> </a></li>
				<li class="facebook"><a href="https://www.facebook.com/groups/1416155608600326/"
					title="like us on facebook">Facebook</a></li>
				<li class="twitter" id="twitter"><a href="#"
					title="follow us on twitter @valkyriesRFC!">Twitter</a></li>
			</ul>
		</div>
		<div id="tweets">
			<a class="twitter-timeline" href="https://twitter.com/valkyriesRFC" data-widget-id="378235035309965312">Tweets by @valkyriesRFC</a>
				<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+"://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>
		</div>
	</header>