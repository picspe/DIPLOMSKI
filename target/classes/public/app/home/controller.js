angular.module('home.controllers', [])
    .controller('homeController', ['$scope', '$rootScope', '$window', '$state', 'homeService',
        function ($scope, $rootScope, $window, $state, homeService) {

            homeService.isLoggedIn().then(null, function () {
                $state.go('login');
            });

            $scope.logout = function () {
                homeService.logout();
            };

            $scope.inboxMail = {
                'id': 1,
                'message': 'hello',
                'date': new Date(),
                'senderMail': 'test.test@sparkmail.com',
                'receiverMail': 'test.test@sparkmail.com',
                'cc': '',
                'subject': 'test'
            };

            $scope.outboxMail = {
                'id': 1,
                'message': 'hello',
                'date': new Date(),
                'senderMail': 'test.test@sparkmail.com',
                'receiverMail': 'test.test@sparkmail.com',
                'cc': '',
                'subject': 'test'
            };

            $scope.messages = [{
                'id': 1,
                'message': 'hello1',
                'date': new Date(),
                'senderMail': 'test.test1@sparkmail.com',
                'receiverMail': 'test.test1@sparkmail.com',
                'cc': '',
                'subject': 'test1'
            }, {
                'id': 1,
                'message': 'hello2',
                'date': new Date(),
                'senderMail': 'test.test2@sparkmail.com',
                'receiverMail': 'test.test2@sparkmail.com',
                'cc': '',
                'subject': 'test2'
            }, {
                'id': 1,
                'message': 'hello3',
                'date': new Date(),
                'senderMail': 'test.test3@sparkmail.com',
                'receiverMail': 'test.3@sparkmail.com',
                'cc': '',
                'subject': 'tes3'
            }, {
                'id': 1,
                'message': 'hello4',
                'date': new Date(),
                'senderMail': 'test.test4@sparkmail.com',
                'receiverMail': 'test.test4@sparkmail.com',
                'cc': '',
                'subject': 'test4'
            }];

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
            };

            $scope.openOutboxMail = function (message) {
                $('.card').addClass('hidden');
                $scope.outboxMail = message;
                $('.outbox').removeClass('hidden');
            };

            $scope.send = function (mail) {
                if (mail.cc || mail.cc !== "") {
                    var receivers = mail.cc.split(';');
                } else {
                    receivers = [mail.receiverMail];
                }
                for (var i in receivers) {
                    mail.receiverMail = receivers[i];
                    homeService.send(mail).then(function (response) {
                        $scope.messages = response.data;
                    })
                }
            };

            function split( val ) {
                return val.split( /,\s*/ );
            }
            function extractLast( term ) {
                return split( term ).pop();
            }

            $('.email-input').on("keydown", function (event) {

                    if (event.keyCode === $.ui.keyCode.TAB &&
                        $(this).autocomplete("instance").menu.active) {
                        event.preventDefault();
                    }


            }).autocomplete({
                minLength: 0,
                source: function (request, response) {
                    var searchTerm = request.term.split(',');
                    searchTerm = searchTerm[searchTerm.length-1].replace(" ","");
                    homeService.getTopTen(searchTerm).then(function (res) {
                        $scope.userList = res.data.map(function(obj) {
                            return obj.email;
                        });
                        // delegate back to autocomplete, but extract the last term
                        response($.ui.autocomplete.filter(
                            $scope.userList, searchTerm));
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
                $scope.newMail.subject = "FW: " + $scope.newMail.subject;
                $scope.newMail.cc = "Cc: " + $scope.newMail.cc + "\n";
                $scope.newMail.message = "Forwarder from: " + mail.senderMail + "\n" + $scope.newMail.message;
                $scope.sendNewToggle();
            };

            $scope.reply = function (mail) {

            };
        }]
    );