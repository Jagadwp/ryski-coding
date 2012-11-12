<?php 
if(session_id() == '') {
	session_set_cookie_params(31536000,'/');
	session_start();
}

include('../include/header.php'); 
include('../include/body.php');
?>
<h1>checkout</h1>
<form name="checkout" class="addressform" method="post"
	action="../controller/submitorder.php">
	<label for="ordertype">order type:</label>
	<select name="ordertype"><option value="1">take out</option>
	<option value="2" selected>delivery</option><option value="3">dine in</option>
	</select>
	<br><br>
	<label for="firstname">first name:</label><input type="text"
		name="firstname" maxlength="255" required="required"> <br> <label
		for="middlename">middle initial:</label><input type="text" name="middle"
		maxlength="255" size="1"> <br> <label for="lastname">last name:</label><input
		type="text" name="lastname" maxlength="255" required="required"> <br>
	<label for="addressline1">address line 1:</label><input type="text"
		name="addressline1" maxlength="255" required="required"> <br> <label
		for="addressline2">address line 2:</label><input type="text"
		name="addressline2" maxlength="255"> <br> <label for="city">city:</label><input
		type="text" name="city" maxlength="25" required="required"> <br> <label
		for="st">state:</label> <select name="state" size="1">
		<option value="AL">alabama</option>
		<option value="AK">alaska</option>
		<option value="AZ">arizona</option>
		<option value="AR">arkansas</option>
		<option value="CA">california</option>
		<option value="CO">colorado</option>
		<option value="CT">connecticut</option>
		<option value="DE">delaware</option>
		<option value="DC">district of columbia</option>
		<option value="FL">florida</option>
		<option value="GA">georgia</option>
		<option value="HI">hawaii</option>
		<option value="ID">idaho</option>
		<option value="IL">illinois</option>
		<option value="IN">indiana</option>
		<option value="IA">iowa</option>
		<option value="KS">kansas</option>
		<option value="KY">kentucky</option>
		<option value="LA">louisiana</option>
		<option value="ME">maine</option>
		<option value="MD">maryland</option>
		<option value="MA" selected>massachusetts</option>
		<option value="MI">michigan</option>
		<option value="MN">minnesota</option>
		<option value="MS">mississippi</option>
		<option value="MO">missouri</option>
		<option value="MT">montana</option>
		<option value="NE">nebraska</option>
		<option value="NV">nevada</option>
		<option value="NH">new hampshire</option>
		<option value="NJ">new jersey</option>
		<option value="NM">new mexico</option>
		<option value="NY">new york</option>
		<option value="NC">north carolina</option>
		<option value="ND">north dakota</option>
		<option value="OH">ohio</option>
		<option value="OK">oklahoma</option>
		<option value="OR">oregon</option>
		<option value="PA">oennsylvania</option>
		<option value="RI">rhode island</option>
		<option value="SC">south carolina</option>
		<option value="SD">south dakota</option>
		<option value="TN">tennessee</option>
		<option value="TX">texas</option>
		<option value="UT">utah</option>
		<option value="VT">vermont</option>
		<option value="VA">virginia</option>
		<option value="WA">washington</option>
		<option value="WV">west virginia</option>
		<option value="WI">wisconsin</option>
		<option value="WY">wyoming</option>
	</select> <br> <label for="zip">zip:</label><input type="text"
		name="zip" maxlength="10" size="10" required="required"> <br> <label
		for="telephone">telephone:</label><input type="tel" name="telephone"
		maxlength="15" required="required"> <br> <br> <label
		for="cctype">credit card type:</label><select name="cctype"
		size="1" required="required"><option value="Visa">visa</option>
		<option value="MasterCard">mastercard</option>
		<option value="American Express">american express</option>
		<option value="Discover">discover</option>
		<option value="Diners">diner's club card</option>
	</select><br> <label for="ccnumber1">credit card number:</label><input
		type="text" size="4" name="ccnumber1"> <input type="text" size="4"
		name="ccnumber2"><input type="text" size="4" name="ccnumber3"><input
		type="text" size="4" name="ccnumber4"> <br> <label for="ccexpmonth">expiration
		date (month/year):</label><select name="ccexpmonth"
		required="required"><option value="January">january</option>
		<option value="February">february</option>
		<option value="March">march</option>
		<option value="April">april</option>
		<option value="May">may</option>
		<option value="June">june</option>
		<option value="July">july</option>
		<option value="August">august</option>
		<option value="September">september</option>
		<option value="October">october</option>
		<option value="November">november</option>
		<option value="December">december</option>
	</select>/<select name="ccexpyear" required="required"><option
			value="2012">2012</option>
		<option value="2013">2013</option>
		<option value="2014">2014</option>
		<option value="2015">2015</option>
		<option value="2016">2016</option>
		<option value="2017">2017</option>
		<option value="2018">2018</option>
		<option value="2019">2019</option>
		<option value="2020">2020</option>
		<option value="2021">2021</option>
		<option value="2022">2022</option>
		<option value="2023">2023</option>
	</select> <br> <br>
	<div class="center">
		<input type="submit" value="finalize your order" >
	</div>
	<br>

</form>
<table>
	<?php
	if (isset($cart)){
		echo "<tr><td class=\"menuitem\">name</td>";
		echo "<td class=\"menudesc\">description</td>";
		echo "<td class=\"menuprice\">price</td>";
		echo "<td class=\"menuquantity\">quantity</td>";
		echo "</tr>";
		$subtotal=0.00;

		foreach($cart as $item){
			$name = $item->getFoodname();
			$desc = $item->getDescription();
			$price = $item->getPrice();
			$subtotal+=$price;
			$price=number_format($price, 2);
			$count=3;
			echo "<tr><td class=\"menuitem\">$name</td>";
			echo "<td class=\"menudesc\">$desc</td>";
			echo "<td class=\"menuprice\">\$$price</td>";
			echo "<td class=\"menuquantity\">$count</td>";
			echo "</tr>";
		}

		echo "<tr class=\"spaceabove\"><td class=\"menuitem\">&nbsp;</td>";
		echo "<td class=\"totaldesc\">Subtotal:</td>";
		$subtotal=number_format($subtotal, 2);
		echo "<td class=\"totalprice\">\$$subtotal</td></tr>";

		echo "<tr><td class=\"menuitem\">&nbsp;</td>";
		echo "<td class=\"totaldesc\">Tax (5.3%):</td>";
		$tax=number_format($subtotal*.053, 2);
		echo "<td class=\"totalprice\">\$$tax</td><td>&nbsp;</td></tr>";

		echo "<tr><td class=\"menuitem\">&nbsp;</td>";
		echo "<td class=\"totaldesc\">Total:</td>";
		$total=$subtotal+$tax;
		echo "<td class=\"totalprice\">\$$total</td></tr>";

	}else {
		echo '<tr><td>Your cart is empty</td></tr>';
	}
	?>
</table>
<?php include('../include/footer.php'); ?>