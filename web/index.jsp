<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/5/18
  Time: 17:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
    <script type="text/javascript" src="http://knockoutjs.com/js/jquery-1.4.2.min.js"></script>
    <script type="text/javascript" src="http://knockoutjs.com/js/jquery.tmpl.js"></script>
    <%--<script type="text/javascript" src="/js/ko.js"></script>--%>
    <script src="http://cdn.bootcss.com/knockout/3.4.0/knockout-debug.js"></script>
</head>
<body>
<p>First name: <strong data-bind="text: firstName"></strong></p>

<p>Last name: <strong data-bind="text: lastName"></strong></p>

<p>First name: <input data-bind="value: firstName"/></p>

<p>Last name: <input data-bind="value: lastName"/></p>

<p>Full name: <strong data-bind="text: fullName"></strong></p>
<script type="text/javascript">
    function AppViewModel() {
        this.firstName = ko.observable("Bert");
        this.lastName = ko.observable("Bertington");

        this.fullName = ko.computed(function () {
            return this.firstName() + " " + this.lastName();
        }, this);
    }
    ko.applyBindings(new AppViewModel());
</script>
</body>
</html>
