angular.module('home.controllers', [])
    .controller('homeController', ['$scope', '$rootScope', '$window', '$state', 'homeService',
        function ($scope, $rootScope, $window, $state, homeService) {
            //init vars
            $scope.newMessageCount = 0;

            homeService.isLoggedIn().then(function () {
                $rootScope.loggedIn = true;
            }, function () {
                $rootScope.loggedIn = false;
                $state.go('login');
            });


            $scope.logout = function () {
                homeService.logout();
            };


            $scope.sendNewToggle = function () {
                $('.card').addClass('hidden');
                $('#inboxnav').addClass('hidden');
                $('#outboxnav').addClass('hidden');
                $('.new-mail').removeClass('hidden');
            };

            $scope.inboxToggle = function () {
                $('.card').addClass('hidden');
                $('#outboxnav').addClass('hidden');
                $('#inboxnav').removeClass('hidden');
                homeService.getInbox().then(function (response) {
                    $scope.messages = response.data;
                });
            };

            $scope.outboxToggle = function () {
                $('.card').addClass('hidden');
                $('#inboxnav').addClass('hidden');
                $('#outboxnav').removeClass('hidden');
                homeService.getOutbox().then(function (response) {
                    $scope.messages = response.data;
                });
            };

            $scope.openInboxMail = function (message) {
                $('.card').addClass('hidden');
                $scope.inboxMail = message;
                $('.inbox').removeClass('hidden');
                homeService.seen(message);
                message.seen = true;
            };

            $scope.openOutboxMail = function (message) {
                $('.card').addClass('hidden');
                $scope.outboxMail = message;
                $('.outbox').removeClass('hidden');
            };

            $scope.send = function (mail) {
                var receivers = $('#receiver').val().split(',');
                mail.cc = $('#ccs').val();
                for (var i in receivers) {
                    if(receivers[i].replace(" ", "")) {
                        mail.receiverMail = receivers[i];
                        homeService.send(mail).then(function (response) {
                            $scope.messages = response.data;
                            $window.location.reload();
                        }, function(response) {
                            alert(response.data);
                            $window.location.reload();
                        });
                    }
                }
            };


            $('.email-input').on("keydown", function (event) {
                if (event.keyCode === $.ui.keyCode.TAB &&
                    $(this).autocomplete("instance").menu.active) {
                    event.preventDefault();
                }
            }).autocomplete({
                minLength: 0,
                source: function (request, response) {
                    var searchTerm = request.term.split(',');
                    searchTerm = searchTerm[searchTerm.length - 1].replace(" ", "");
                    homeService.getTopTen(searchTerm).then(function (res) {
                        $scope.userList = res.data.map(function (obj) {
                            return obj.email;
                        });
                        // delegate back to autocomplete, but extract the last term
                        response($.ui.autocomplete.filter($scope.userList, searchTerm));
                    });
                },
                focus: function () {
                    // prevent value inserted on focus
                    return false;
                },
                select: function (event, ui) {
                    var terms = this.value.split(',');
                    // remove the current input
                    terms.pop();
                    // add the selected item
                    terms.push(ui.item.value);
                    // add placeholder to get the comma-and-space at the end
                    terms.push("");
                    this.value = terms.join(", ");
                    return false;
                }
            });

            $scope.forward = function (mail) {
                $scope.newMail = Object.assign({}, mail);
                $scope.newMail.receiverMail = "";
                $scope.newMail.subject = "FW: " + $scope.newMail.subject;
                $scope.newMail.message ="\n" +
                    "______________ Forwarded message ______________\n" +
                    "From: " + mail.senderMail + "\n" +
                    "Date: " + mail.date + "\n" +
                    "Subject: " + mail.subject + "\n" +
                    "To : " + mail.receiverMail + "\n" +
                    "Message : " + mail.message + "\n " +
                    "__________________________________________________\n";
                $scope.sendNewToggle();
                $("#receiver").focus();
            };

            $scope.reply = function (mail) {
                $scope.newMail = Object.assign({}, mail);
                $scope.newMail.subject = "RE: " + $scope.newMail.subject;
                $scope.newMail.message = "From: " + mail.senderMail + "\nMessage: \"" + mail.message + "\"\n\nReply: ";
                $scope.sendNewToggle();
                $("#messageBox").focus();
            };

            $scope.delete = function (mail) {
                homeService.delete(mail).then(function() {
                    $window.location.reload();
                })
            };
        }]
    );